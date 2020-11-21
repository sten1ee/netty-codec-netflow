/**
 * Copyright (C) 2017 Jeremy Custenborder (jcustenborder@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jcustenborder.netty.netflow.v9;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NetFlowV9Decoder extends MessageToMessageDecoder<DatagramPacket>
                              implements NetFlow {
  private static final Logger log = LoggerFactory.getLogger(NetFlowV9Decoder.class);

  final NetFlow.Factory netflowFactory;

  public NetFlowV9Decoder(NetFlow.Factory netflowFactory) {
    this.netflowFactory = netflowFactory;
  }

  public NetFlowV9Decoder() {
    this(new NetFlowFactoryImpl());
  }

  Header decodeHeader(ByteBuf b, InetSocketAddress sender, InetSocketAddress recipient) {
    ByteBuf input = b.readSlice(20);

    short version = input.readShort();
    short count = input.readShort();
    int uptime = input.readInt();
    int timestamp = input.readInt();
    int flowSequence = input.readInt();
    int sourceID = input.readInt();

    log.trace("version = {} count = {} uptime = {} timestamp = {} flowSequence = {} sourceID = {}",
        version, count, uptime, timestamp, flowSequence, sourceID
    );

    checkReadFully(input);
    return new Header(version, count, uptime, timestamp, flowSequence, sourceID, sender, recipient);
  }

  private void checkReadFully(ByteBuf input) {
    if (input.readableBytes() > 0) {
      throw new IllegalStateException(
          String.format("input has %s bytes remaining.", input.readableBytes())
      );
    }
  }

  TemplateFlowSet decodeTemplate(ByteBuf b, short flowSetID) {
    int length = b.readShort() - 4;
    log.trace("readSlice({})", length);
    ByteBuf input = b.readSlice(length);

    short templateID = input.readShort();
    short fieldCount = input.readShort();
    log.trace("templateID = {} fieldCount = {}", templateID, fieldCount);
    List<TemplateField> fields = new ArrayList<>(fieldCount);
    int fieldOffset = 0;
    for (short j = 1; j <= fieldCount; j++) {
      short fieldType = input.readShort();
      short fieldLength = input.readShort();
      log.trace("field({}/{}): type = {} length = {} offset = {}", j, fieldCount, fieldType, fieldLength, fieldOffset);

      TemplateField templateField = this.netflowFactory.templateField(fieldType, fieldLength, fieldOffset);
      fields.add(templateField);
      fieldOffset += fieldLength;
    }
    checkReadFully(input);
    return this.netflowFactory.templateFlowSet(flowSetID, templateID, fields);
  }

  DataFlowSet decodeData(ByteBuf b, short flowSetID, TemplateFlowSet template) {
    int length = b.readShort() - 4;
    log.trace("readSlice({})", length);
    ByteBuf input = b.readSlice(length);
    byte[] data = new byte[length];
    input.readBytes(data);

    /*// <Dump -------------------------------->
    CiscoFieldScheme fieldScheme = new CiscoFieldScheme();
    Map<Field, Object> dict = fieldScheme.parse(netflowFactory.dataFlowSet(flowSetID, data, template));
    int fieldCount = template.fields().size();
    for (int i = 0; i < fieldCount; ++i) {
      TemplateField tf = template.fields().get(i);
      Field field = fieldScheme.getField(tf.type());
      if (field != null) {
        log.trace("\tfieldValue({}/{}): {}: {}",
                  i, fieldCount, field, dict.get(field));
      } else {
        log.trace("\tfieldValue({}/{}): <<noname-field>>, typeId: {}",
                  i, fieldCount, tf.type());
      }
    }
    // </Dump -------------------------------->*/
    return this.netflowFactory.dataFlowSet(flowSetID, data, template);
  }

  @Override
  protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List<Object> output) throws Exception {
    ByteBuf input = datagramPacket.content();

    if (null == input || !input.isReadable()) {
      log.trace("Message from {} was not usable.", datagramPacket.sender());
      return;
    }

    Header header = decodeHeader(input, datagramPacket.sender(), datagramPacket.recipient());

    log.trace("Read {} for header. {} remaining", input.readerIndex(), input.readableBytes());

    List<FlowSet> flowSets = new ArrayList<>();
    Map<Short, TemplateFlowSet> templateByIdMap = new HashMap<>();

    while (input.readableBytes() > 0) {
      short flowsetID = input.readShort();
      log.trace("Processing flowset {}", flowsetID);

      if (0 == flowsetID) {
        TemplateFlowSet templateFlowSet = decodeTemplate(input, flowsetID);
        flowSets.add(templateFlowSet);
        templateByIdMap.put(templateFlowSet.templateID(), templateFlowSet);
      } else {
        TemplateFlowSet template = templateByIdMap.get(flowsetID);
        if (template != null) {
          DataFlowSet dataFlowSet = decodeData(input, flowsetID, template);
          flowSets.add(dataFlowSet);
        } else {
          // According to Cisco's doc template-less data flows should be discarded:
          log.warn("Discarded data flow that refers to an undefined templateID: {}", flowsetID);
        }
      }

      log.trace("Read {}. Available {}", input.readerIndex(), input.readableBytes());
    }

    Message message = this.netflowFactory.netflowMessage(header, flowSets);
    output.add(message);
  }
}

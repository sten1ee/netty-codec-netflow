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

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

class NetFlowFactoryImpl implements NetFlowV9.Factory, NetFlowV9 {

  @Override
  public Message netflowMessage(Header header, List<FlowSet> flowsets, Map<Short, TemplateFlowSet> templateByIdMap) {
    return new MessageImpl(header, flowsets, templateByIdMap);
  }

  @Override
  public TemplateField templateField(short type, short length, int offset) {
    return new TemplateFieldImpl(type, length, offset);
  }

  @Override
  public TemplateFlowSet templateFlowSet(short flowsetID, short templateID, List<TemplateField> fields) {
    return new TemplateFlowSetImpl(flowsetID, templateID, fields);
  }

  @Override
  public DataFlowSet dataFlowSet(short flowsetID, byte[] data) {
    return new DataFlowSetImpl(flowsetID, data);
  }

  static class MessageImpl implements Message {
    final Header header;
    final List<FlowSet> flowsets;
    final Map<Short, TemplateFlowSet> templateByIdMap;

    MessageImpl(Header header, List<FlowSet> flowsets, Map<Short, TemplateFlowSet> templateByIdMap) {
      this.header = header;
      this.flowsets = flowsets;
      this.templateByIdMap = templateByIdMap;
    }

    @Override
    public short version() {
      return header.version;
    }

    @Override
    public short count() {
      return header.count;
    }

    @Override
    public int uptime() {
      return header.uptime;
    }

    @Override
    public int timestamp() {
      return header.timestamp;
    }

    @Override
    public int flowSequence() {
      return header.flowSequence;
    }

    @Override
    public int sourceID() {
      return header.sourceID;
    }

    @Override
    public InetSocketAddress sender() {
      return header.sender;
    }

    @Override
    public InetSocketAddress recipient() {
      return header.recipient;
    }

    @Override
    public List<FlowSet> flowsets() {
      return flowsets;
    }

    @Override
    public TemplateFlowSet templateById(short templateId) {
      return templateByIdMap.get(templateId);
    }
  }

  static class TemplateFieldImpl implements TemplateField {
    final short type;
    final short length;
    final int offset;

    TemplateFieldImpl(short type, short length, int offset) {
      this.type = type;
      this.length = length;
      this.offset = offset;
    }

    @Override
    public short type() {
      return this.type;
    }

    @Override
    public short length() {
      return this.length;
    }

    @Override
    public int offset() {
      return this.offset;
    }
  }

  static class TemplateFlowSetImpl implements TemplateFlowSet {
    final short flowsetID;
    final short templateID;
    final List<TemplateField> fields;

    TemplateFlowSetImpl(short flowsetID, short templateID, List<TemplateField> fields) {
      this.flowsetID = flowsetID;
      this.templateID = templateID;
      this.fields = fields;
    }

    @Override
    public short flowsetID() {
      return this.flowsetID;
    }

    @Override
    public short templateID() {
      return this.templateID;
    }

    @Override
    public List<TemplateField> fields() {
      return this.fields;
    }
  }

  static class DataFlowSetImpl implements DataFlowSet {
    final short flowsetID;
    final byte[] data;

    DataFlowSetImpl(short flowsetID, byte[] data) {
      this.flowsetID = flowsetID;
      this.data = data;
    }

    @Override
    public short flowsetID() {
      return this.flowsetID;
    }

    @Override
    public byte[] data() {
      return this.data;
    }
  }
}

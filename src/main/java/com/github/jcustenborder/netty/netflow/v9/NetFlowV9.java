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

/**
 * This a hollow/namespace interface that contains all the interfaces related to NetFlowV9
 */
public interface NetFlowV9 {
  final class Header {
    final short version;
    final short count;
    final int uptime;
    final int timestamp;
    final int flowSequence;
    final int sourceID;
    final InetSocketAddress sender;
    final InetSocketAddress recipient;

    Header(short version, short count, int uptime, int timestamp, int flowSequence, int sourceID, InetSocketAddress sender, InetSocketAddress recipient) {
      this.version = version;
      this.count = count;
      this.uptime = uptime;
      this.timestamp = timestamp;
      this.flowSequence = flowSequence;
      this.sourceID = sourceID;
      this.sender = sender;
      this.recipient = recipient;
    }
  }

  interface Message {
    short version();

    short count();

    int uptime();

    int timestamp();

    int flowSequence();

    int sourceID();

    InetSocketAddress sender();

    InetSocketAddress recipient();

    List<FlowSet> flowsets();

    TemplateFlowSet templateById(short templateId);
  }

  interface FlowSet {
    short flowsetID();
  }

  interface TemplateField {
    short type();

    // field length (in bytes)
    short length();

    // field offset (in bytes) relative to FlowSet data block beginning
    int offset();
  }

  interface TemplateFlowSet extends FlowSet {
    short templateID();

    List<TemplateField> fields();
  }


  interface DataFlowSet extends FlowSet {
    byte[] data();

    TemplateFlowSet template();
  }

  interface Factory {
    Message netflowMessage(Header header, List<FlowSet> flowsets, Map<Short, TemplateFlowSet> templateByIdMap);

    TemplateField templateField(short type, short length, int offset);

    TemplateFlowSet templateFlowSet(short flowsetID, short templateID, List<TemplateField> fields);

    DataFlowSet dataFlowSet(short flowsetID, byte[] data, TemplateFlowSet template);
  }

  final class FieldType {
    final String name;
    final short type;   // type id in the current FieldTypeScheme
    final short length; // 0 length indicates variable field length

    FieldType(String fieldName, short fieldType, short fieldLength) {
      name = fieldName;
      type = fieldType;
      length = fieldLength;
    }
  }

  /**
   * A set of FieldType(s) that maps type id to type name and field length
   */
  interface FieldTypeScheme {
    FieldType getFieldType(int type);
  }
}
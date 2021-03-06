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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * This is a 'namespace' interface that contains all the interfaces (and a DTO class) related to NetFlow
 */
public interface NetFlow {
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
    Message netflowMessage(Header header, List<FlowSet> flowsets);

    TemplateField templateField(short type, short length, int offset);

    TemplateFlowSet templateFlowSet(short flowsetID, short templateID, List<TemplateField> fields);

    DataFlowSet dataFlowSet(short flowsetID, byte[] data, TemplateFlowSet template);
  }


  interface Field {
    String name();

    DataType dataType();

    enum DataType {
      ASCII_STRING,
      HEX_BYTE,
      BYTE,
      SHORT,
      INTEGER,
      BIG_INTEGER,
      MAC_ADDR,
      IPV4_ADDR,
      IPV6_ADDR,
      BYTE_ARRAY,
    }
  }

  /**
   * A set of Field(s) that maps field type id to field name and data type
   */
  interface FieldScheme {

    /**
     * Get Field based on its (integer) typeId.
     * https://www.cisco.com/en/US/technologies/tk648/tk362/technologies_white_paper09186a00800a3db9.html#wp9001622
     * @param typeId
     * @return the corresponding Field or null
     */
    Field getField(int typeId);

    Charset ASCII = Charset.forName("US-ASCII");

    /**
     * Parse a (binary) DataFlowSet into a Field-Value pairs Model
     * which is convenient for further processing - displaying, aggregating etc
     * @param dataFlowSet the dataFlowSet to parse (should have a non-null template!)
     * @return the parsed Model which is effectively a Field to Value mapping (e.g. see CiscoFieldScheme's enum Field)
     */
    default LinkedHashMap<Field, Object> parse(DataFlowSet dataFlowSet) {
      LinkedHashMap<Field, Object> model = new LinkedHashMap<>();

      if (dataFlowSet.template() == null) {
        // Should never happen as Cisco's doc mandates:
        throw new NullPointerException("Template-less data flows should have been discarded at creation time!");
      }

      for (TemplateField templateField : dataFlowSet.template().fields()) {
        Field field = getField(templateField.type());
        if (field == null) {
          Logger log = LoggerFactory.getLogger(this.getClass());
          log.warn("Unknown Field typeId: {} in FieldScheme {}", templateField.type(), this);
          continue;
        }
        byte[] data = dataFlowSet.data();
        int off = templateField.offset();
        int len = templateField.length();
        Object value;
      SWITCH:
        switch (field.dataType()) {
          case BYTE_ARRAY:
          case ASCII_STRING:
            value = new String(data, off, len, ASCII);
            break;

          case MAC_ADDR:
            assertThat(len == 6);
            value = String.format("%02x:%02x:%02x:%02x:%02x:%02x",
                                  data[off + 0] & 0xFF, data[off + 1] & 0xFF, data[off + 2] & 0xFF,
                                  data[off + 3] & 0xFF, data[off + 4] & 0xFF, data[off + 5] & 0xFF);
            break;

          case IPV4_ADDR:
            assertThat(len == 4);
            value = String.format("%d.%d.%d.%d",
                                  data[off + 0] & 0xFF, data[off + 1] & 0xFF, data[off + 2] & 0xFF, data[off + 3] & 0xFF);
            break;

          case IPV6_ADDR:
            assertThat(len == 16);
            value = String.format("%02X%02X:%02X%02X:%02X%02X:%02X%02X:%02X%02X:%02X%02X:%02X%02X:%02X%02X",
                                  data[off + 0] & 0xFF, data[off + 1] & 0xFF, data[off + 2] & 0xFF, data[off + 3] & 0xFF,
                                  data[off + 4] & 0xFF, data[off + 5] & 0xFF, data[off + 6] & 0xFF, data[off + 7] & 0xFF,
                                  data[off + 8] & 0xFF, data[off + 9] & 0xFF, data[off + 10] & 0xFF, data[off + 11] & 0xFF,
                                  data[off + 12] & 0xFF, data[off + 13] & 0xFF, data[off + 14] & 0xFF, data[off + 15] & 0xFF);
            break;

          case HEX_BYTE:
            value = String.format("0x%02X", data[off]);
            break;

          case BYTE:
          case SHORT:
          case INTEGER:
          case BIG_INTEGER:
            value = parseNumber(data, off, len);
            break;

          default:
            assertThat(false, "Field " + field + " has unexpected dataType: " + field.dataType());
            value = null;
        }

        if (value != null) {
          model.put(field, value);
        }
      }

      return model;
    }

    /**
     *  Parse a (mostly unsigned) number into appropriate Java integral type (Byte, Short, Int, Long or BigInteger)
     */
    default Number parseNumber(byte[] data, int off, int len) {
      if (len > 8 || len == 8 && data[0] < 0) { // Long will not suffice
        // Delegate the job to BigInteger:
        // Note - the way we handle it here may result in a negative value!
        return new BigInteger(Arrays.copyOfRange(data, off, off + len));
      }

      // network is BIG ENDIAN!
      long val = 0;
      for (int i = 0; i < len; ++i) {
        val <<= 8;
        val |= data[off + i] & 0xFF;
      }

      if (val >= Integer.MIN_VALUE && val <= Integer.MAX_VALUE) {
        if (val >= Short.MIN_VALUE && val <= Short.MAX_VALUE) {
          if (val >= Byte.MIN_VALUE && val <= Byte.MAX_VALUE) {
            return (byte) val;
          } else {
            return (short) val;
          }
        } else {
          return (int) val;
        }
      } else {
        return (long) val;
      }
    }

    static void assertThat(boolean condition, String msg) throws IllegalArgumentException {
      if (!condition) {
        throw new IllegalArgumentException(msg);
      }
    }

    static void assertThat(boolean condition) throws IllegalArgumentException {
      if (!condition) {
        throw new IllegalArgumentException("Unexpected");
      }
    }
  }
}

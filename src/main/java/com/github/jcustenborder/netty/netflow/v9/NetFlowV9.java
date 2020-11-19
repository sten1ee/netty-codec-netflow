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

import java.util.HashMap;
import java.util.Map;

/**
 * Interface to host the FieldDescriptor[] fields array
 */
public interface NetFlowV9 {
  class FieldDescriptor {
    final String name;
    final int id;
    final int byteSize;
    
    FieldDescriptor(String fieldName, int fieldId, int fieldByteSize) {
      name = fieldName;
      id = fieldId;
      byteSize = fieldByteSize;
    }
  }
  
  static FieldDescriptor fieldDescriptor(String name, int id, int byteSize) {
    FieldDescriptor fieldDsc = new FieldDescriptor(name, id, byteSize);
    FieldDescriptor old = FIELD_BY_ID.put(id, fieldDsc);
    assert old == null : "Duplicate Field name " + name;
    return fieldDsc;
  }

  Map<Integer, FieldDescriptor> FIELD_BY_ID = new HashMap<>();
  /**
   * Following is based on Table 6 here:
   * https://www.cisco.com/en/US/technologies/tk648/tk362/technologies_white_paper09186a00800a3db9.html#wp9001622
   */
  FieldDescriptor[] FIELDS = {
    fieldDescriptor("RESERVED",                      0,   0),
    fieldDescriptor("IN_BYTES",                      1,   0),
    fieldDescriptor("IN_PKTS",                       2,   0),
    fieldDescriptor("FLOWS",                         3,   0),
    fieldDescriptor("PROTOCOL",                      4,   1),
    fieldDescriptor("TOS",                           5,   1),
    fieldDescriptor("TCP_FLAGS",                     6,   1),
    fieldDescriptor("L4_SRC_PORT",                   7,   2),
    fieldDescriptor("IPV4_SRC_ADDR",                 8,   4),
    fieldDescriptor("SRC_MASK",                      9,   1),
    fieldDescriptor("INPUT_SNMP",                   10,   0),
    fieldDescriptor("L4_DST_PORT",                  11,   2),
    fieldDescriptor("IPV4_DST_ADDR",                12,   4),
    fieldDescriptor("DST_MASK",                     13,   1),
    fieldDescriptor("OUTPUT_SNMP",                  14,   0),
    fieldDescriptor("IPV4_NEXT_HOP",                15,   4),
    fieldDescriptor("SRC_AS",                       16,   0),
    fieldDescriptor("DST_AS",                       17,   0),
    fieldDescriptor("BGP_IPV4_NEXT_HOP",            18,   4),
    fieldDescriptor("MUL_DST_PKTS",                 19,   0),
    fieldDescriptor("MUL_DST_BYTES",                20,   0),
    fieldDescriptor("LAST_SWITCHED",                21,   4),
    fieldDescriptor("FIRST_SWITCHED",               22,   4),
    fieldDescriptor("OUT_BYTES",                    23,   0),
    fieldDescriptor("OUT_PKTS",                     24,   0),
    fieldDescriptor("MIN_PKT_LNGTH",                25,   2),
    fieldDescriptor("MAX_PKT_LNGTH",                26,   2),
    fieldDescriptor("IPV6_SRC_ADDR",                27,  16),
    fieldDescriptor("IPV6_DST_ADDR",                28,  16),
    fieldDescriptor("IPV6_SRC_MASK",                29,   1),
    fieldDescriptor("IPV6_DST_MASK",                30,   1),
    fieldDescriptor("IPV6_FLOW_LABEL",              31,   3),
    fieldDescriptor("ICMP_TYPE",                    32,   2),
    fieldDescriptor("MUL_IGMP_TYPE",                33,   1),
    fieldDescriptor("SAMPLING_INTERVAL",            34,   4),
    fieldDescriptor("SAMPLING_ALGORITHM",           35,   1),
    fieldDescriptor("FLOW_ACTIVE_TIMEOUT",          36,   2),
    fieldDescriptor("FLOW_INACTIVE_TIMEOUT",        37,   2),
    fieldDescriptor("ENGINE_TYPE",                  38,   1),
    fieldDescriptor("ENGINE_ID",                    39,   1),
    fieldDescriptor("TOTAL_BYTES_EXP",              40,   0),
    fieldDescriptor("TOTAL_PKTS_EXP",               41,   0),
    fieldDescriptor("TOTAL_FLOWS_EXP",              42,   0),
    fieldDescriptor("VENDOR_PROPRIETARY",           43,   0), /* length? */
    fieldDescriptor("IPV4_SRC_PREFIX",              44,   4),
    fieldDescriptor("IPV4_DST_PREFIX",              45,   4),
    fieldDescriptor("MPLS_TOP_LABEL_TYPE",          46,   1),
    fieldDescriptor("MPLS_TOP_LABEL_IP_ADDR",       47,   4),
    fieldDescriptor("FLOW_SAMPLER_ID",              48,   1),
    fieldDescriptor("FLOW_SAMPLER_MODE",            49,   1),
    fieldDescriptor("FLOW_SAMPLER_RANDOM_INTERVAL", 50,   4),
    fieldDescriptor("VENDOR_PROPRIETARY",           51,   0), /* length? */
    fieldDescriptor("MIN_TTL",                      52,   1),
    fieldDescriptor("MAX_TTL",                      53,   1),
    fieldDescriptor("IPV4_IDENT",                   54,   2),
    fieldDescriptor("DST_TOS",                      55,   1),
    fieldDescriptor("SRC_MAC",                      56,   6),
    fieldDescriptor("DST_MAC",                      57,   6),
    fieldDescriptor("SRC_VLAN",                     58,   2),
    fieldDescriptor("DST_VLAN",                     59,   2),
    fieldDescriptor("IP_PROTOCOL_VERSION",          60,   1),
    fieldDescriptor("DIRECTION",                    61,   1),
    fieldDescriptor("IPV6_NEXT_HOP",                62,  16),
    fieldDescriptor("BGP_IPV6_NEXT_HOP",            63,  16),
    fieldDescriptor("IPV6_OPTION_HEADERS",          64,   4),
    fieldDescriptor("VENDOR_PROPRIETARY",           65,   0), /* length? */
    fieldDescriptor("VENDOR_PROPRIETARY",           66,   0), /* length? */
    fieldDescriptor("VENDOR_PROPRIETARY",           67,   0), /* length? */
    fieldDescriptor("VENDOR_PROPRIETARY",           68,   0), /* length? */
    fieldDescriptor("VENDOR_PROPRIETARY",           69,   0), /* length? */
    fieldDescriptor("MPLS_LABEL_1",                 70,   3),
    fieldDescriptor("MPLS_LABEL_2",                 71,   3),
    fieldDescriptor("MPLS_LABEL_3",                 72,   3),
    fieldDescriptor("MPLS_LABEL_4",                 73,   3),
    fieldDescriptor("MPLS_LABEL_5",                 74,   3),
    fieldDescriptor("MPLS_LABEL_6",                 75,   3),
    fieldDescriptor("MPLS_LABEL_7",                 76,   3),
    fieldDescriptor("MPLS_LABEL_8",                 77,   3),
    fieldDescriptor("MPLS_LABEL_9",                 78,   3),
    fieldDescriptor("MPLS_LABEL_10",                79,   3),
    fieldDescriptor("IN_DST_MAC",                   80,   6),
    fieldDescriptor("OUT_SRC_MAC",                  81,   6),
    fieldDescriptor("IF_NAME",                      82,   0),
    fieldDescriptor("IF_DESC",                      83,   0),
    fieldDescriptor("SAMPLER_NAME",                 84,   0),
    fieldDescriptor("IN_PERMANENT_BYTES",           85,   0),
    fieldDescriptor("IN_PERMANENT_PKTS",            86,   0),
    fieldDescriptor("VENDOR_PROPRIETARY",           87,   0), /* length? */
    fieldDescriptor("FRAGMENT_OFFSET",              88,   2),
    fieldDescriptor("FORWARDING_STATUS",            89,   1),
    fieldDescriptor("MPLS_PAL_RD",                  90,   8),
    fieldDescriptor("MPLS_PREFIX_LEN",              91,   1),
    fieldDescriptor("SRC_TRAFFIC_INDEX",            92,   4),
    fieldDescriptor("DST_TRAFFIC_INDEX",            93,   4),
    fieldDescriptor("APPLICATION_DESCRIPTION",      94,   0),
    fieldDescriptor("APPLICATION_TAG",              95,   0), /* 1+n ? */
    fieldDescriptor("APPLICATION_NAME",             96,   0),
    fieldDescriptor("VENDOR_PROPRIETARY",           97,   0), /* length? */
    fieldDescriptor("postipDiffServCodePoint",      98,   1),
    fieldDescriptor("replication_factor",           99,   4),
    fieldDescriptor("DEPRECATED",                  100,   0),
    fieldDescriptor("RESERVED",                    101,   0),
    fieldDescriptor("layer2packetSectionOffset",   102,   0),
    fieldDescriptor("layer2packetSectionSize",     103,   0),
    fieldDescriptor("layer2packetSectionData",     104,   0),
    fieldDescriptor("SALT",                        105, 200), /* NONSTANDARD */
  };
}

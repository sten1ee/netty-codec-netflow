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


/**
 * Cisco's NetFlow v9 Field Type scheme as descried in this doc (table 6):
 * https://www.cisco.com/en/US/technologies/tk648/tk362/technologies_white_paper09186a00800a3db9.html#wp9001622
 */
public class CiscoFieldTypeScheme implements NetFlowV9.FieldTypeScheme {

  @Override
  public NetFlowV9.FieldType getFieldType(int type) {
    return CISCO_FIELD_TYPES[type];
  }

  /**
   * Based on Table 6 here:
   * https://www.cisco.com/en/US/technologies/tk648/tk362/technologies_white_paper09186a00800a3db9.html#wp9001622
   */
  private static final NetFlowV9.FieldType[] CISCO_FIELD_TYPES = new NetFlowV9.FieldType[106];

  static {
    regFieldType("RESERVED",                      0,   0);
    regFieldType("IN_BYTES",                      1,   0);
    regFieldType("IN_PKTS",                       2,   0);
    regFieldType("FLOWS",                         3,   0);
    regFieldType("PROTOCOL",                      4,   1);
    regFieldType("TOS",                           5,   1);
    regFieldType("TCP_FLAGS",                     6,   1);
    regFieldType("L4_SRC_PORT",                   7,   2);
    regFieldType("IPV4_SRC_ADDR",                 8,   4);
    regFieldType("SRC_MASK",                      9,   1);
    regFieldType("INPUT_SNMP",                   10,   0);
    regFieldType("L4_DST_PORT",                  11,   2);
    regFieldType("IPV4_DST_ADDR",                12,   4);
    regFieldType("DST_MASK",                     13,   1);
    regFieldType("OUTPUT_SNMP",                  14,   0);
    regFieldType("IPV4_NEXT_HOP",                15,   4);
    regFieldType("SRC_AS",                       16,   0);
    regFieldType("DST_AS",                       17,   0);
    regFieldType("BGP_IPV4_NEXT_HOP",            18,   4);
    regFieldType("MUL_DST_PKTS",                 19,   0);
    regFieldType("MUL_DST_BYTES",                20,   0);
    regFieldType("LAST_SWITCHED",                21,   4);
    regFieldType("FIRST_SWITCHED",               22,   4);
    regFieldType("OUT_BYTES",                    23,   0);
    regFieldType("OUT_PKTS",                     24,   0);
    regFieldType("MIN_PKT_LNGTH",                25,   2);
    regFieldType("MAX_PKT_LNGTH",                26,   2);
    regFieldType("IPV6_SRC_ADDR",                27,  16);
    regFieldType("IPV6_DST_ADDR",                28,  16);
    regFieldType("IPV6_SRC_MASK",                29,   1);
    regFieldType("IPV6_DST_MASK",                30,   1);
    regFieldType("IPV6_FLOW_LABEL",              31,   3);
    regFieldType("ICMP_TYPE",                    32,   2);
    regFieldType("MUL_IGMP_TYPE",                33,   1);
    regFieldType("SAMPLING_INTERVAL",            34,   4);
    regFieldType("SAMPLING_ALGORITHM",           35,   1);
    regFieldType("FLOW_ACTIVE_TIMEOUT",          36,   2);
    regFieldType("FLOW_INACTIVE_TIMEOUT",        37,   2);
    regFieldType("ENGINE_TYPE",                  38,   1);
    regFieldType("ENGINE_ID",                    39,   1);
    regFieldType("TOTAL_BYTES_EXP",              40,   0);
    regFieldType("TOTAL_PKTS_EXP",               41,   0);
    regFieldType("TOTAL_FLOWS_EXP",              42,   0);
    regFieldType("VENDOR_PROPRIETARY",           43,   0); /* length? */
    regFieldType("IPV4_SRC_PREFIX",              44,   4);
    regFieldType("IPV4_DST_PREFIX",              45,   4);
    regFieldType("MPLS_TOP_LABEL_TYPE",          46,   1);
    regFieldType("MPLS_TOP_LABEL_IP_ADDR",       47,   4);
    regFieldType("FLOW_SAMPLER_ID",              48,   1);
    regFieldType("FLOW_SAMPLER_MODE",            49,   1);
    regFieldType("FLOW_SAMPLER_RANDOM_INTERVAL", 50,   4);
    regFieldType("VENDOR_PROPRIETARY",           51,   0); /* length? */
    regFieldType("MIN_TTL",                      52,   1);
    regFieldType("MAX_TTL",                      53,   1);
    regFieldType("IPV4_IDENT",                   54,   2);
    regFieldType("DST_TOS",                      55,   1);
    regFieldType("SRC_MAC",                      56,   6);
    regFieldType("DST_MAC",                      57,   6);
    regFieldType("SRC_VLAN",                     58,   2);
    regFieldType("DST_VLAN",                     59,   2);
    regFieldType("IP_PROTOCOL_VERSION",          60,   1);
    regFieldType("DIRECTION",                    61,   1);
    regFieldType("IPV6_NEXT_HOP",                62,  16);
    regFieldType("BGP_IPV6_NEXT_HOP",            63,  16);
    regFieldType("IPV6_OPTION_HEADERS",          64,   4);
    regFieldType("VENDOR_PROPRIETARY",           65,   0); /* length? */
    regFieldType("VENDOR_PROPRIETARY",           66,   0); /* length? */
    regFieldType("VENDOR_PROPRIETARY",           67,   0); /* length? */
    regFieldType("VENDOR_PROPRIETARY",           68,   0); /* length? */
    regFieldType("VENDOR_PROPRIETARY",           69,   0); /* length? */
    regFieldType("MPLS_LABEL_1",                 70,   3);
    regFieldType("MPLS_LABEL_2",                 71,   3);
    regFieldType("MPLS_LABEL_3",                 72,   3);
    regFieldType("MPLS_LABEL_4",                 73,   3);
    regFieldType("MPLS_LABEL_5",                 74,   3);
    regFieldType("MPLS_LABEL_6",                 75,   3);
    regFieldType("MPLS_LABEL_7",                 76,   3);
    regFieldType("MPLS_LABEL_8",                 77,   3);
    regFieldType("MPLS_LABEL_9",                 78,   3);
    regFieldType("MPLS_LABEL_10",                79,   3);
    regFieldType("IN_DST_MAC",                   80,   6);
    regFieldType("OUT_SRC_MAC",                  81,   6);
    regFieldType("IF_NAME",                      82,   0);
    regFieldType("IF_DESC",                      83,   0);
    regFieldType("SAMPLER_NAME",                 84,   0);
    regFieldType("IN_PERMANENT_BYTES",           85,   0);
    regFieldType("IN_PERMANENT_PKTS",            86,   0);
    regFieldType("VENDOR_PROPRIETARY",           87,   0); /* length? */
    regFieldType("FRAGMENT_OFFSET",              88,   2);
    regFieldType("FORWARDING_STATUS",            89,   1);
    regFieldType("MPLS_PAL_RD",                  90,   8);
    regFieldType("MPLS_PREFIX_LEN",              91,   1);
    regFieldType("SRC_TRAFFIC_INDEX",            92,   4);
    regFieldType("DST_TRAFFIC_INDEX",            93,   4);
    regFieldType("APPLICATION_DESCRIPTION",      94,   0);
    regFieldType("APPLICATION_TAG",              95,   0); /* 1+n ? */
    regFieldType("APPLICATION_NAME",             96,   0);
    regFieldType("VENDOR_PROPRIETARY",           97,   0); /* length? */
    regFieldType("postipDiffServCodePoint",      98,   1);
    regFieldType("replication_factor",           99,   4);
    regFieldType("DEPRECATED",                  100,   0);
    regFieldType("RESERVED",                    101,   0);
    regFieldType("layer2packetSectionOffset",   102,   0);
    regFieldType("layer2packetSectionSize",     103,   0);
    regFieldType("layer2packetSectionData",     104,   0);
    regFieldType("SALT",                        105, 200); /* NONSTANDARD */
  }

  private static void regFieldType(String name, int type, int length) {
    if (type < 0 || type > Short.MAX_VALUE) {
      throw new Error("'type' is out of range: " + type);
    }
    if (length < 0 || length > Short.MAX_VALUE) {
      throw new Error("'length' is out of range: " + length);
    }
    if (CISCO_FIELD_TYPES[type] != null) {
      throw new Error("Duplicate FieldType definition, type: " + type);
    }

    CISCO_FIELD_TYPES[type] = new NetFlowV9.FieldType(name, (short) type, (short) length);
  }

  public static void main(String[] args) {
    for (int i = 0; i < CISCO_FIELD_TYPES.length; ++i) {
      if (CISCO_FIELD_TYPES[i] == null) {
        throw new Error("Missing FieldType def for type " + i);
      }
      if (CISCO_FIELD_TYPES[i].type != i) {
        throw new Error("FieldType.type mismatch for type " + i);
      }
    }

    if (!CISCO_FIELD_TYPES[8].name.equals("IPV4_SRC_ADDR")) {
      throw new Error("Unexpected FieldType.name for type " + 8);
    }
  }
}

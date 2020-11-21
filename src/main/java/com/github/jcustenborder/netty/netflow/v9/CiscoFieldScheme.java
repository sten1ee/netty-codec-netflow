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

import static com.github.jcustenborder.netty.netflow.v9.NetFlowV9.FieldScheme.assertThat;
/**
 * Cisco's NetFlow v9 Field Type scheme as descried in this doc (table 6):
 * https://www.cisco.com/en/US/technologies/tk648/tk362/technologies_white_paper09186a00800a3db9.html#wp9001622
 */
public class CiscoFieldScheme implements NetFlowV9.FieldScheme<CiscoFieldScheme.Field> {

  @Override
  public Field getField(int typeId) {
    return CISCO_FIELD_TYPES[typeId];
  }

  private static final Field[] CISCO_FIELD_TYPES = Field.values();

  /**
   * Based on Table 6 here:
   * https://www.cisco.com/en/US/technologies/tk648/tk362/technologies_white_paper09186a00800a3db9.html#wp9001622
   */
  enum Field implements NetFlowV9.Field {
    RESERVED1                     (0,   0),
    IN_BYTES                      (1,   0, DataType.BIG_INTEGER),
    IN_PKTS                       (2,   0, DataType.BIG_INTEGER),
    FLOWS                         (3,   0, DataType.BIG_INTEGER),
    PROTOCOL                      (4,   1, DataType.BYTE),
    TOS                           (5,   1, DataType.HEX_BYTE),
    TCP_FLAGS                     (6,   1, DataType.HEX_BYTE),
    L4_SRC_PORT                   (7,   2, DataType.SHORT),
    IPV4_SRC_ADDR                 (8,   4, DataType.IPV4_ADDR),
    SRC_MASK                      (9,   1, DataType.HEX_BYTE),
    INPUT_SNMP                   (10,   0),
    L4_DST_PORT                  (11,   2, DataType.SHORT),
    IPV4_DST_ADDR                (12,   4, DataType.IPV4_ADDR),
    DST_MASK                     (13,   1, DataType.HEX_BYTE),
    OUTPUT_SNMP                  (14,   0),
    IPV4_NEXT_HOP                (15,   4, DataType.IPV4_ADDR),
    SRC_AS                       (16,   0),
    DST_AS                       (17,   0),
    BGP_IPV4_NEXT_HOP            (18,   4, DataType.IPV4_ADDR),
    MUL_DST_PKTS                 (19,   0),
    MUL_DST_BYTES                (20,   0),
    LAST_SWITCHED                (21,   4, DataType.INTEGER),
    FIRST_SWITCHED               (22,   4, DataType.INTEGER),
    OUT_BYTES                    (23,   0),
    OUT_PKTS                     (24,   0),
    MIN_PKT_LNGTH                (25,   2, DataType.SHORT),
    MAX_PKT_LNGTH                (26,   2, DataType.SHORT),
    IPV6_SRC_ADDR                (27,  16, DataType.IPV6_ADDR),
    IPV6_DST_ADDR                (28,  16, DataType.IPV6_ADDR),
    IPV6_SRC_MASK                (29,   1),
    IPV6_DST_MASK                (30,   1),
    IPV6_FLOW_LABEL              (31,   3),
    ICMP_TYPE                    (32,   2),
    MUL_IGMP_TYPE                (33,   1),
    SAMPLING_INTERVAL            (34,   4),
    SAMPLING_ALGORITHM           (35,   1),
    FLOW_ACTIVE_TIMEOUT          (36,   2),
    FLOW_INACTIVE_TIMEOUT        (37,   2),
    ENGINE_TYPE                  (38,   1),
    ENGINE_ID                    (39,   1),
    TOTAL_BYTES_EXP              (40,   0),
    TOTAL_PKTS_EXP               (41,   0),
    TOTAL_FLOWS_EXP              (42,   0),
    VENDOR_PROPRIETARY1          (43,   0), /* length? */
    IPV4_SRC_PREFIX              (44,   4),
    IPV4_DST_PREFIX              (45,   4),
    MPLS_TOP_LABEL_TYPE          (46,   1),
    MPLS_TOP_LABEL_IP_ADDR       (47,   4),
    FLOW_SAMPLER_ID              (48,   1),
    FLOW_SAMPLER_MODE            (49,   1),
    FLOW_SAMPLER_RANDOM_INTERVAL (50,   4),
    VENDOR_PROPRIETARY2          (51,   0), /* length? */
    MIN_TTL                      (52,   1),
    MAX_TTL                      (53,   1),
    IPV4_IDENT                   (54,   2),
    DST_TOS                      (55,   1),
    SRC_MAC                      (56,   6, DataType.MAC_ADDR),
    DST_MAC                      (57,   6, DataType.MAC_ADDR),
    SRC_VLAN                     (58,   2),
    DST_VLAN                     (59,   2),
    IP_PROTOCOL_VERSION          (60,   1, DataType.BYTE),
    DIRECTION                    (61,   1, DataType.BYTE),
    IPV6_NEXT_HOP                (62,  16, DataType.IPV6_ADDR),
    BGP_IPV6_NEXT_HOP            (63,  16, DataType.IPV6_ADDR),
    IPV6_OPTION_HEADERS          (64,   4),
    VENDOR_PROPRIETARY3          (65,   0), /* length? */
    VENDOR_PROPRIETARY4          (66,   0), /* length? */
    VENDOR_PROPRIETARY5          (67,   0), /* length? */
    VENDOR_PROPRIETARY6          (68,   0), /* length? */
    VENDOR_PROPRIETARY7          (69,   0), /* length? */
    MPLS_LABEL_1                 (70,   3),
    MPLS_LABEL_2                 (71,   3),
    MPLS_LABEL_3                 (72,   3),
    MPLS_LABEL_4                 (73,   3),
    MPLS_LABEL_5                 (74,   3),
    MPLS_LABEL_6                 (75,   3),
    MPLS_LABEL_7                 (76,   3),
    MPLS_LABEL_8                 (77,   3),
    MPLS_LABEL_9                 (78,   3),
    MPLS_LABEL_10                (79,   3),
    IN_DST_MAC                   (80,   6, DataType.MAC_ADDR),
    OUT_SRC_MAC                  (81,   6, DataType.MAC_ADDR),
    IF_NAME                      (82,   0, DataType.ASCII_STRING),
    IF_DESC                      (83,   0, DataType.ASCII_STRING),
    SAMPLER_NAME                 (84,   0, DataType.ASCII_STRING),
    IN_PERMANENT_BYTES           (85,   0),
    IN_PERMANENT_PKTS            (86,   0),
    VENDOR_PROPRIETARY8          (87,   0), /* length? */
    FRAGMENT_OFFSET              (88,   2),
    FORWARDING_STATUS            (89,   1),
    MPLS_PAL_RD                  (90,   8),
    MPLS_PREFIX_LEN              (91,   1),
    SRC_TRAFFIC_INDEX            (92,   4),
    DST_TRAFFIC_INDEX            (93,   4),
    APPLICATION_DESCRIPTION      (94,   0, DataType.ASCII_STRING),
    APPLICATION_TAG              (95,   0), /* 1+n ? */
    APPLICATION_NAME             (96,   0, DataType.ASCII_STRING),
    VENDOR_PROPRIETARY9          (97,   0), /* length? */
    postipDiffServCodePoint      (98,   1),
    replication_factor           (99,   4),
    DEPRECATED                  (100,   0),
    RESERVED2                   (101,   0),
    layer2packetSectionOffset   (102,   0),
    layer2packetSectionSize     (103,   0),
    layer2packetSectionData     (104,   0);
    //SALT                        (105, 200), /* NONSTANDARD */

    Field(int typeId, int length, DataType dataType) {
      this.typeId = (short) typeId;
      this.length = (short) length;
      this.dataType = dataType;

      assertThat(typeId >= 0 && typeId <= Short.MAX_VALUE, "'typeId' is out of range for " + this);
      assertThat(length >= 0 && length <= Short.MAX_VALUE, "'length' is out of range for " + this);
      assertThat(dataType != null, "'dataType' is null for " + this);
    }

    Field(int typeId, int length) {
      this(typeId, length, DataType.BYTE_ARRAY);
    }

    final int typeId; // type id in the current FieldScheme
    final int length; // 0 length indicates variable field length
    final DataType dataType;

    @Override
    public DataType dataType() {
      return dataType;
    }
  }

  static {
    for (int i = 0; i < CISCO_FIELD_TYPES.length; ++i) {
      assertThat(CISCO_FIELD_TYPES[i] != null, "Missing Field def at index " + i);
      assertThat(CISCO_FIELD_TYPES[i].typeId == i, "typeId != index at index " + i);
    }
    assertThat(CISCO_FIELD_TYPES[8].name().equals("IPV4_SRC_ADDR"), "Unexpected Field.name for typeId " + 8);
  }

  public static void main(String[] args) {
  }
}

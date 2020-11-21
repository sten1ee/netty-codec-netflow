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
package com.github.jcustenborder.netty.netflow.v9.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.jcustenborder.netty.netflow.v9.NetFlow;

import java.net.InetSocketAddress;

public class ObjectMapperSingleton {
  public static ObjectMapper instance;

  static {
    ObjectMapper objectMapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();

    module.addSerializer(NetFlow.Message.class, new NetflowMessageStorage.Serializer());
    module.addDeserializer(NetFlow.Message.class, new NetflowMessageStorage.Deserializer());

//    module.addSerializer(NetFlow.DataFlowSet.class, new DataFlowSet.Serializer());
//    module.addDeserializer(NetFlow.DataFlowSet.class, new DataFlowSet.Deserializer());
//
//    module.addSerializer(NetFlow.TemplateFlowSet.class, new TemplateFlowSet.Serializer());
//    module.addDeserializer(NetFlow.TemplateFlowSet.class, new TemplateFlowSet.Deserializer());

    module.addSerializer(NetFlow.FlowSet.class, new FlowSetStorage.Serializer());
    module.addDeserializer(NetFlow.FlowSet.class, new FlowSetStorage.Deserializer());

    module.addSerializer(NetFlow.TemplateField.class, new TemplateFieldStorage.Serializer());
    module.addDeserializer(NetFlow.TemplateField.class, new TemplateFieldStorage.Deserializer());

    module.addSerializer(InetSocketAddress.class, new InetSocketAddressStorage.Serializer());
    module.addDeserializer(InetSocketAddress.class, new InetSocketAddressStorage.Deserializer());

    objectMapper.registerModule(module);
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

    instance = objectMapper;
  }

}

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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.jcustenborder.netty.netflow.v9.NetFlow;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlowSetStorage {
  public enum FlowSetType {
    Template,
    Data
  }

  public FlowSetType type;

  public short flowsetID;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  public Short templateID;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public List<NetFlow.TemplateField> fields;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public byte[] data;

  public static class Serializer extends JsonSerializer<NetFlow.FlowSet> {
    @Override
    public void serialize(NetFlow.FlowSet flowSet, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
      FlowSetStorage storage = new FlowSetStorage();

      if (flowSet instanceof NetFlow.TemplateFlowSet) {
        storage.type = FlowSetType.Template;
        storage.fields = ((NetFlow.TemplateFlowSet) flowSet).fields();
        storage.templateID = ((NetFlow.TemplateFlowSet) flowSet).templateID();
      } else if (flowSet instanceof NetFlow.DataFlowSet) {
        storage.type = FlowSetType.Data;
        storage.data = ((NetFlow.DataFlowSet) flowSet).data();
      } else {
        throw new UnsupportedOperationException();
      }

      storage.flowsetID = flowSet.flowsetID();

      jsonGenerator.writeObject(storage);
    }
  }

  public static class Deserializer extends JsonDeserializer<NetFlow.FlowSet> {

    @Override
    public NetFlow.FlowSet deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
      FlowSetStorage storage = jsonParser.readValueAs(FlowSetStorage.class);

      if (FlowSetType.Template == storage.type) {
        NetFlow.TemplateFlowSet flowSet = mock(NetFlow.TemplateFlowSet.class);
        when(flowSet.flowsetID()).thenReturn(storage.flowsetID);
        when(flowSet.templateID()).thenReturn(storage.templateID);
        when(flowSet.fields()).thenReturn(storage.fields);
        return flowSet;
      } else if (FlowSetType.Data == storage.type) {
        NetFlow.DataFlowSet flowSet = mock(NetFlow.DataFlowSet.class);
        when(flowSet.flowsetID()).thenReturn(storage.flowsetID);
        when(flowSet.data()).thenReturn(storage.data);
        return flowSet;
      } else {
        throw new UnsupportedOperationException();
      }
    }
  }

}

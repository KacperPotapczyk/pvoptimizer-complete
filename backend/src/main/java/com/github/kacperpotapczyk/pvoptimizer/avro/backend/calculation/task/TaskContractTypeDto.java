/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task;
@org.apache.avro.specific.AvroGenerated
public enum TaskContractTypeDto implements org.apache.avro.generic.GenericEnumSymbol<TaskContractTypeDto> {
  PURCHASE, SELL  ;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"enum\",\"name\":\"TaskContractTypeDto\",\"namespace\":\"com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task\",\"symbols\":[\"PURCHASE\",\"SELL\"]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
}

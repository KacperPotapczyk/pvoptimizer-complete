/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result;
@org.apache.avro.specific.AvroGenerated
public enum TaskCalculationResultStatusDto implements org.apache.avro.generic.GenericEnumSymbol<TaskCalculationResultStatusDto> {
  SOLUTION_FOUND, SOLUTION_NOT_FOUND, OPTIMIZER_ERROR  ;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"enum\",\"name\":\"TaskCalculationResultStatusDto\",\"namespace\":\"com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result\",\"symbols\":[\"SOLUTION_FOUND\",\"SOLUTION_NOT_FOUND\",\"OPTIMIZER_ERROR\"]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
}

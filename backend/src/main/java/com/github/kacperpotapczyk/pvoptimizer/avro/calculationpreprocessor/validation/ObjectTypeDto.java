/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.github.kacperpotapczyk.pvoptimizer.avro.calculationpreprocessor.validation;
@org.apache.avro.specific.AvroGenerated
public enum ObjectTypeDto implements org.apache.avro.generic.GenericEnumSymbol<ObjectTypeDto> {
  CONTRACT, DEMAND, TARIFF, PRODUCTION, STORAGE, MOVABLE_DEMAND  ;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"enum\",\"name\":\"ObjectTypeDto\",\"namespace\":\"com.github.kacperpotapczyk.pvoptimizer.avro.calculationpreprocessor.validation\",\"symbols\":[\"CONTRACT\",\"DEMAND\",\"TARIFF\",\"PRODUCTION\",\"STORAGE\",\"MOVABLE_DEMAND\"]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
}

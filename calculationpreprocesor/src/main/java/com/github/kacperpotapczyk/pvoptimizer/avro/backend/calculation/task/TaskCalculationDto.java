/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class TaskCalculationDto extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -81840265243879322L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"TaskCalculationDto\",\"namespace\":\"com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task\",\"fields\":[{\"name\":\"id\",\"type\":\"long\",\"doc\":\"Task identifier\"},{\"name\":\"dateTimeStart\",\"type\":\"string\",\"doc\":\"Task start date and time\"},{\"name\":\"dateTimeEnd\",\"type\":\"string\",\"doc\":\"Task end date and time\"},{\"name\":\"contracts\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"TaskContractDto\",\"fields\":[{\"name\":\"id\",\"type\":\"long\",\"doc\":\"Contract id\"},{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Contract name\"},{\"name\":\"revisionNumber\",\"type\":\"long\",\"doc\":\"Revision number of contract\"},{\"name\":\"contractType\",\"type\":{\"type\":\"enum\",\"name\":\"TaskContractTypeDto\",\"symbols\":[\"PURCHASE\",\"SELL\"]},\"doc\":\"Defines if it is purchase or sell contract\"},{\"name\":\"tariffName\",\"type\":\"string\",\"doc\":\"Contract tariff\"},{\"name\":\"minPowerConstraints\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"TaskContractConstraintDto\",\"fields\":[{\"name\":\"constraintValue\",\"type\":\"double\",\"doc\":\"Constraint value\"},{\"name\":\"dateTimeStart\",\"type\":\"string\",\"doc\":\"Date and time of constraint start\"},{\"name\":\"dateTimeEnd\",\"type\":\"string\",\"doc\":\"Date and time of constraint end\"}]}},\"doc\":\"Minimal power constraints of contract\",\"default\":[]},{\"name\":\"maxPowerConstraints\",\"type\":{\"type\":\"array\",\"items\":\"TaskContractConstraintDto\"},\"doc\":\"Maximal power constraints of contract\",\"default\":[]},{\"name\":\"minEnergyConstraints\",\"type\":{\"type\":\"array\",\"items\":\"TaskContractConstraintDto\"},\"doc\":\"Minimal energy constraints of contract\",\"default\":[]},{\"name\":\"maxEnergyConstraints\",\"type\":{\"type\":\"array\",\"items\":\"TaskContractConstraintDto\"},\"doc\":\"Maximal energy constraints of contract\",\"default\":[]}]}},\"doc\":\"List of electricity purchase and sell contracts.\",\"default\":[]},{\"name\":\"demands\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"TaskDemandDto\",\"fields\":[{\"name\":\"id\",\"type\":\"long\",\"doc\":\"Demand Id\"},{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Demand name\"},{\"name\":\"revisionNumber\",\"type\":\"long\",\"doc\":\"Demand revision number\"},{\"name\":\"demandValues\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"TaskDemandValueDto\",\"fields\":[{\"name\":\"value\",\"type\":\"double\",\"doc\":\"Demand value on timestamp\"},{\"name\":\"dateTime\",\"type\":\"string\",\"doc\":\"Demand value timestamp\"}]}},\"doc\":\"Demand values on timestamp list\",\"default\":[]}]}},\"doc\":\"List of household demands.\",\"default\":[]},{\"name\":\"tariffs\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"TaskTariffDto\",\"doc\":\"Contract tariff\",\"fields\":[{\"name\":\"id\",\"type\":\"long\",\"doc\":\"Tariff id\"},{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Tariff name\"},{\"name\":\"revisionNumber\",\"type\":\"long\",\"doc\":\"Tariff revision number\"},{\"name\":\"defaultPrice\",\"type\":\"double\",\"doc\":\"Default unit price\"}]}},\"doc\":\"List of task tariffs\",\"default\":[]}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<TaskCalculationDto> ENCODER =
      new BinaryMessageEncoder<>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<TaskCalculationDto> DECODER =
      new BinaryMessageDecoder<>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<TaskCalculationDto> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<TaskCalculationDto> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<TaskCalculationDto> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this TaskCalculationDto to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a TaskCalculationDto from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a TaskCalculationDto instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static TaskCalculationDto fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  /** Task identifier */
  private long id;
  /** Task start date and time */
  private java.lang.CharSequence dateTimeStart;
  /** Task end date and time */
  private java.lang.CharSequence dateTimeEnd;
  /** List of electricity purchase and sell contracts. */
  private java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractDto> contracts;
  /** List of household demands. */
  private java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskDemandDto> demands;
  /** List of task tariffs */
  private java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskTariffDto> tariffs;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public TaskCalculationDto() {}

  /**
   * All-args constructor.
   * @param id Task identifier
   * @param dateTimeStart Task start date and time
   * @param dateTimeEnd Task end date and time
   * @param contracts List of electricity purchase and sell contracts.
   * @param demands List of household demands.
   * @param tariffs List of task tariffs
   */
  public TaskCalculationDto(java.lang.Long id, java.lang.CharSequence dateTimeStart, java.lang.CharSequence dateTimeEnd, java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractDto> contracts, java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskDemandDto> demands, java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskTariffDto> tariffs) {
    this.id = id;
    this.dateTimeStart = dateTimeStart;
    this.dateTimeEnd = dateTimeEnd;
    this.contracts = contracts;
    this.demands = demands;
    this.tariffs = tariffs;
  }

  @Override
  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }

  // Used by DatumWriter.  Applications should not call.
  @Override
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return id;
    case 1: return dateTimeStart;
    case 2: return dateTimeEnd;
    case 3: return contracts;
    case 4: return demands;
    case 5: return tariffs;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @Override
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: id = (java.lang.Long)value$; break;
    case 1: dateTimeStart = (java.lang.CharSequence)value$; break;
    case 2: dateTimeEnd = (java.lang.CharSequence)value$; break;
    case 3: contracts = (java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractDto>)value$; break;
    case 4: demands = (java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskDemandDto>)value$; break;
    case 5: tariffs = (java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskTariffDto>)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'id' field.
   * @return Task identifier
   */
  public long getId() {
    return id;
  }


  /**
   * Sets the value of the 'id' field.
   * Task identifier
   * @param value the value to set.
   */
  public void setId(long value) {
    this.id = value;
  }

  /**
   * Gets the value of the 'dateTimeStart' field.
   * @return Task start date and time
   */
  public java.lang.CharSequence getDateTimeStart() {
    return dateTimeStart;
  }


  /**
   * Sets the value of the 'dateTimeStart' field.
   * Task start date and time
   * @param value the value to set.
   */
  public void setDateTimeStart(java.lang.CharSequence value) {
    this.dateTimeStart = value;
  }

  /**
   * Gets the value of the 'dateTimeEnd' field.
   * @return Task end date and time
   */
  public java.lang.CharSequence getDateTimeEnd() {
    return dateTimeEnd;
  }


  /**
   * Sets the value of the 'dateTimeEnd' field.
   * Task end date and time
   * @param value the value to set.
   */
  public void setDateTimeEnd(java.lang.CharSequence value) {
    this.dateTimeEnd = value;
  }

  /**
   * Gets the value of the 'contracts' field.
   * @return List of electricity purchase and sell contracts.
   */
  public java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractDto> getContracts() {
    return contracts;
  }


  /**
   * Sets the value of the 'contracts' field.
   * List of electricity purchase and sell contracts.
   * @param value the value to set.
   */
  public void setContracts(java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractDto> value) {
    this.contracts = value;
  }

  /**
   * Gets the value of the 'demands' field.
   * @return List of household demands.
   */
  public java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskDemandDto> getDemands() {
    return demands;
  }


  /**
   * Sets the value of the 'demands' field.
   * List of household demands.
   * @param value the value to set.
   */
  public void setDemands(java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskDemandDto> value) {
    this.demands = value;
  }

  /**
   * Gets the value of the 'tariffs' field.
   * @return List of task tariffs
   */
  public java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskTariffDto> getTariffs() {
    return tariffs;
  }


  /**
   * Sets the value of the 'tariffs' field.
   * List of task tariffs
   * @param value the value to set.
   */
  public void setTariffs(java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskTariffDto> value) {
    this.tariffs = value;
  }

  /**
   * Creates a new TaskCalculationDto RecordBuilder.
   * @return A new TaskCalculationDto RecordBuilder
   */
  public static com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto.Builder newBuilder() {
    return new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto.Builder();
  }

  /**
   * Creates a new TaskCalculationDto RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new TaskCalculationDto RecordBuilder
   */
  public static com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto.Builder newBuilder(com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto.Builder other) {
    if (other == null) {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto.Builder();
    } else {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto.Builder(other);
    }
  }

  /**
   * Creates a new TaskCalculationDto RecordBuilder by copying an existing TaskCalculationDto instance.
   * @param other The existing instance to copy.
   * @return A new TaskCalculationDto RecordBuilder
   */
  public static com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto.Builder newBuilder(com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto other) {
    if (other == null) {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto.Builder();
    } else {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto.Builder(other);
    }
  }

  /**
   * RecordBuilder for TaskCalculationDto instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<TaskCalculationDto>
    implements org.apache.avro.data.RecordBuilder<TaskCalculationDto> {

    /** Task identifier */
    private long id;
    /** Task start date and time */
    private java.lang.CharSequence dateTimeStart;
    /** Task end date and time */
    private java.lang.CharSequence dateTimeEnd;
    /** List of electricity purchase and sell contracts. */
    private java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractDto> contracts;
    /** List of household demands. */
    private java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskDemandDto> demands;
    /** List of task tariffs */
    private java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskTariffDto> tariffs;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.dateTimeStart)) {
        this.dateTimeStart = data().deepCopy(fields()[1].schema(), other.dateTimeStart);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.dateTimeEnd)) {
        this.dateTimeEnd = data().deepCopy(fields()[2].schema(), other.dateTimeEnd);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.contracts)) {
        this.contracts = data().deepCopy(fields()[3].schema(), other.contracts);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
      if (isValidValue(fields()[4], other.demands)) {
        this.demands = data().deepCopy(fields()[4].schema(), other.demands);
        fieldSetFlags()[4] = other.fieldSetFlags()[4];
      }
      if (isValidValue(fields()[5], other.tariffs)) {
        this.tariffs = data().deepCopy(fields()[5].schema(), other.tariffs);
        fieldSetFlags()[5] = other.fieldSetFlags()[5];
      }
    }

    /**
     * Creates a Builder by copying an existing TaskCalculationDto instance
     * @param other The existing instance to copy.
     */
    private Builder(com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.dateTimeStart)) {
        this.dateTimeStart = data().deepCopy(fields()[1].schema(), other.dateTimeStart);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.dateTimeEnd)) {
        this.dateTimeEnd = data().deepCopy(fields()[2].schema(), other.dateTimeEnd);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.contracts)) {
        this.contracts = data().deepCopy(fields()[3].schema(), other.contracts);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.demands)) {
        this.demands = data().deepCopy(fields()[4].schema(), other.demands);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.tariffs)) {
        this.tariffs = data().deepCopy(fields()[5].schema(), other.tariffs);
        fieldSetFlags()[5] = true;
      }
    }

    /**
      * Gets the value of the 'id' field.
      * Task identifier
      * @return The value.
      */
    public long getId() {
      return id;
    }


    /**
      * Sets the value of the 'id' field.
      * Task identifier
      * @param value The value of 'id'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto.Builder setId(long value) {
      validate(fields()[0], value);
      this.id = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'id' field has been set.
      * Task identifier
      * @return True if the 'id' field has been set, false otherwise.
      */
    public boolean hasId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'id' field.
      * Task identifier
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto.Builder clearId() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'dateTimeStart' field.
      * Task start date and time
      * @return The value.
      */
    public java.lang.CharSequence getDateTimeStart() {
      return dateTimeStart;
    }


    /**
      * Sets the value of the 'dateTimeStart' field.
      * Task start date and time
      * @param value The value of 'dateTimeStart'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto.Builder setDateTimeStart(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.dateTimeStart = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'dateTimeStart' field has been set.
      * Task start date and time
      * @return True if the 'dateTimeStart' field has been set, false otherwise.
      */
    public boolean hasDateTimeStart() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'dateTimeStart' field.
      * Task start date and time
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto.Builder clearDateTimeStart() {
      dateTimeStart = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'dateTimeEnd' field.
      * Task end date and time
      * @return The value.
      */
    public java.lang.CharSequence getDateTimeEnd() {
      return dateTimeEnd;
    }


    /**
      * Sets the value of the 'dateTimeEnd' field.
      * Task end date and time
      * @param value The value of 'dateTimeEnd'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto.Builder setDateTimeEnd(java.lang.CharSequence value) {
      validate(fields()[2], value);
      this.dateTimeEnd = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'dateTimeEnd' field has been set.
      * Task end date and time
      * @return True if the 'dateTimeEnd' field has been set, false otherwise.
      */
    public boolean hasDateTimeEnd() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'dateTimeEnd' field.
      * Task end date and time
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto.Builder clearDateTimeEnd() {
      dateTimeEnd = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'contracts' field.
      * List of electricity purchase and sell contracts.
      * @return The value.
      */
    public java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractDto> getContracts() {
      return contracts;
    }


    /**
      * Sets the value of the 'contracts' field.
      * List of electricity purchase and sell contracts.
      * @param value The value of 'contracts'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto.Builder setContracts(java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractDto> value) {
      validate(fields()[3], value);
      this.contracts = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'contracts' field has been set.
      * List of electricity purchase and sell contracts.
      * @return True if the 'contracts' field has been set, false otherwise.
      */
    public boolean hasContracts() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'contracts' field.
      * List of electricity purchase and sell contracts.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto.Builder clearContracts() {
      contracts = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'demands' field.
      * List of household demands.
      * @return The value.
      */
    public java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskDemandDto> getDemands() {
      return demands;
    }


    /**
      * Sets the value of the 'demands' field.
      * List of household demands.
      * @param value The value of 'demands'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto.Builder setDemands(java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskDemandDto> value) {
      validate(fields()[4], value);
      this.demands = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'demands' field has been set.
      * List of household demands.
      * @return True if the 'demands' field has been set, false otherwise.
      */
    public boolean hasDemands() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'demands' field.
      * List of household demands.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto.Builder clearDemands() {
      demands = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    /**
      * Gets the value of the 'tariffs' field.
      * List of task tariffs
      * @return The value.
      */
    public java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskTariffDto> getTariffs() {
      return tariffs;
    }


    /**
      * Sets the value of the 'tariffs' field.
      * List of task tariffs
      * @param value The value of 'tariffs'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto.Builder setTariffs(java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskTariffDto> value) {
      validate(fields()[5], value);
      this.tariffs = value;
      fieldSetFlags()[5] = true;
      return this;
    }

    /**
      * Checks whether the 'tariffs' field has been set.
      * List of task tariffs
      * @return True if the 'tariffs' field has been set, false otherwise.
      */
    public boolean hasTariffs() {
      return fieldSetFlags()[5];
    }


    /**
      * Clears the value of the 'tariffs' field.
      * List of task tariffs
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto.Builder clearTariffs() {
      tariffs = null;
      fieldSetFlags()[5] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TaskCalculationDto build() {
      try {
        TaskCalculationDto record = new TaskCalculationDto();
        record.id = fieldSetFlags()[0] ? this.id : (java.lang.Long) defaultValue(fields()[0]);
        record.dateTimeStart = fieldSetFlags()[1] ? this.dateTimeStart : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.dateTimeEnd = fieldSetFlags()[2] ? this.dateTimeEnd : (java.lang.CharSequence) defaultValue(fields()[2]);
        record.contracts = fieldSetFlags()[3] ? this.contracts : (java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractDto>) defaultValue(fields()[3]);
        record.demands = fieldSetFlags()[4] ? this.demands : (java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskDemandDto>) defaultValue(fields()[4]);
        record.tariffs = fieldSetFlags()[5] ? this.tariffs : (java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskTariffDto>) defaultValue(fields()[5]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<TaskCalculationDto>
    WRITER$ = (org.apache.avro.io.DatumWriter<TaskCalculationDto>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<TaskCalculationDto>
    READER$ = (org.apache.avro.io.DatumReader<TaskCalculationDto>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeLong(this.id);

    out.writeString(this.dateTimeStart);

    out.writeString(this.dateTimeEnd);

    long size0 = this.contracts.size();
    out.writeArrayStart();
    out.setItemCount(size0);
    long actualSize0 = 0;
    for (com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractDto e0: this.contracts) {
      actualSize0++;
      out.startItem();
      e0.customEncode(out);
    }
    out.writeArrayEnd();
    if (actualSize0 != size0)
      throw new java.util.ConcurrentModificationException("Array-size written was " + size0 + ", but element count was " + actualSize0 + ".");

    long size1 = this.demands.size();
    out.writeArrayStart();
    out.setItemCount(size1);
    long actualSize1 = 0;
    for (com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskDemandDto e1: this.demands) {
      actualSize1++;
      out.startItem();
      e1.customEncode(out);
    }
    out.writeArrayEnd();
    if (actualSize1 != size1)
      throw new java.util.ConcurrentModificationException("Array-size written was " + size1 + ", but element count was " + actualSize1 + ".");

    long size2 = this.tariffs.size();
    out.writeArrayStart();
    out.setItemCount(size2);
    long actualSize2 = 0;
    for (com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskTariffDto e2: this.tariffs) {
      actualSize2++;
      out.startItem();
      e2.customEncode(out);
    }
    out.writeArrayEnd();
    if (actualSize2 != size2)
      throw new java.util.ConcurrentModificationException("Array-size written was " + size2 + ", but element count was " + actualSize2 + ".");

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.id = in.readLong();

      this.dateTimeStart = in.readString(this.dateTimeStart instanceof Utf8 ? (Utf8)this.dateTimeStart : null);

      this.dateTimeEnd = in.readString(this.dateTimeEnd instanceof Utf8 ? (Utf8)this.dateTimeEnd : null);

      long size0 = in.readArrayStart();
      java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractDto> a0 = this.contracts;
      if (a0 == null) {
        a0 = new SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractDto>((int)size0, SCHEMA$.getField("contracts").schema());
        this.contracts = a0;
      } else a0.clear();
      SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractDto> ga0 = (a0 instanceof SpecificData.Array ? (SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractDto>)a0 : null);
      for ( ; 0 < size0; size0 = in.arrayNext()) {
        for ( ; size0 != 0; size0--) {
          com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractDto e0 = (ga0 != null ? ga0.peek() : null);
          if (e0 == null) {
            e0 = new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractDto();
          }
          e0.customDecode(in);
          a0.add(e0);
        }
      }

      long size1 = in.readArrayStart();
      java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskDemandDto> a1 = this.demands;
      if (a1 == null) {
        a1 = new SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskDemandDto>((int)size1, SCHEMA$.getField("demands").schema());
        this.demands = a1;
      } else a1.clear();
      SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskDemandDto> ga1 = (a1 instanceof SpecificData.Array ? (SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskDemandDto>)a1 : null);
      for ( ; 0 < size1; size1 = in.arrayNext()) {
        for ( ; size1 != 0; size1--) {
          com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskDemandDto e1 = (ga1 != null ? ga1.peek() : null);
          if (e1 == null) {
            e1 = new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskDemandDto();
          }
          e1.customDecode(in);
          a1.add(e1);
        }
      }

      long size2 = in.readArrayStart();
      java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskTariffDto> a2 = this.tariffs;
      if (a2 == null) {
        a2 = new SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskTariffDto>((int)size2, SCHEMA$.getField("tariffs").schema());
        this.tariffs = a2;
      } else a2.clear();
      SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskTariffDto> ga2 = (a2 instanceof SpecificData.Array ? (SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskTariffDto>)a2 : null);
      for ( ; 0 < size2; size2 = in.arrayNext()) {
        for ( ; size2 != 0; size2--) {
          com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskTariffDto e2 = (ga2 != null ? ga2.peek() : null);
          if (e2 == null) {
            e2 = new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskTariffDto();
          }
          e2.customDecode(in);
          a2.add(e2);
        }
      }

    } else {
      for (int i = 0; i < 6; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.id = in.readLong();
          break;

        case 1:
          this.dateTimeStart = in.readString(this.dateTimeStart instanceof Utf8 ? (Utf8)this.dateTimeStart : null);
          break;

        case 2:
          this.dateTimeEnd = in.readString(this.dateTimeEnd instanceof Utf8 ? (Utf8)this.dateTimeEnd : null);
          break;

        case 3:
          long size0 = in.readArrayStart();
          java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractDto> a0 = this.contracts;
          if (a0 == null) {
            a0 = new SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractDto>((int)size0, SCHEMA$.getField("contracts").schema());
            this.contracts = a0;
          } else a0.clear();
          SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractDto> ga0 = (a0 instanceof SpecificData.Array ? (SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractDto>)a0 : null);
          for ( ; 0 < size0; size0 = in.arrayNext()) {
            for ( ; size0 != 0; size0--) {
              com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractDto e0 = (ga0 != null ? ga0.peek() : null);
              if (e0 == null) {
                e0 = new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractDto();
              }
              e0.customDecode(in);
              a0.add(e0);
            }
          }
          break;

        case 4:
          long size1 = in.readArrayStart();
          java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskDemandDto> a1 = this.demands;
          if (a1 == null) {
            a1 = new SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskDemandDto>((int)size1, SCHEMA$.getField("demands").schema());
            this.demands = a1;
          } else a1.clear();
          SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskDemandDto> ga1 = (a1 instanceof SpecificData.Array ? (SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskDemandDto>)a1 : null);
          for ( ; 0 < size1; size1 = in.arrayNext()) {
            for ( ; size1 != 0; size1--) {
              com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskDemandDto e1 = (ga1 != null ? ga1.peek() : null);
              if (e1 == null) {
                e1 = new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskDemandDto();
              }
              e1.customDecode(in);
              a1.add(e1);
            }
          }
          break;

        case 5:
          long size2 = in.readArrayStart();
          java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskTariffDto> a2 = this.tariffs;
          if (a2 == null) {
            a2 = new SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskTariffDto>((int)size2, SCHEMA$.getField("tariffs").schema());
            this.tariffs = a2;
          } else a2.clear();
          SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskTariffDto> ga2 = (a2 instanceof SpecificData.Array ? (SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskTariffDto>)a2 : null);
          for ( ; 0 < size2; size2 = in.arrayNext()) {
            for ( ; size2 != 0; size2--) {
              com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskTariffDto e2 = (ga2 != null ? ga2.peek() : null);
              if (e2 == null) {
                e2 = new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskTariffDto();
              }
              e2.customDecode(in);
              a2.add(e2);
            }
          }
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}











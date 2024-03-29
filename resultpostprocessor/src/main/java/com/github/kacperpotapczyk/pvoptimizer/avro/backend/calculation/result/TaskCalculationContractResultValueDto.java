/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class TaskCalculationContractResultValueDto extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -294852386446368427L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"TaskCalculationContractResultValueDto\",\"namespace\":\"com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result\",\"fields\":[{\"name\":\"dateTimeStart\",\"type\":\"string\",\"doc\":\"Interval start date time\"},{\"name\":\"dateTimeEnd\",\"type\":\"string\",\"doc\":\"Interval end date time\"},{\"name\":\"power\",\"type\":\"double\",\"doc\":\"Interval power value\"},{\"name\":\"energy\",\"type\":\"double\",\"doc\":\"Interval energy value\"},{\"name\":\"cost\",\"type\":\"double\",\"doc\":\"Interval cost/income value\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<TaskCalculationContractResultValueDto> ENCODER =
      new BinaryMessageEncoder<>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<TaskCalculationContractResultValueDto> DECODER =
      new BinaryMessageDecoder<>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<TaskCalculationContractResultValueDto> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<TaskCalculationContractResultValueDto> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<TaskCalculationContractResultValueDto> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this TaskCalculationContractResultValueDto to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a TaskCalculationContractResultValueDto from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a TaskCalculationContractResultValueDto instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static TaskCalculationContractResultValueDto fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  /** Interval start date time */
  private java.lang.CharSequence dateTimeStart;
  /** Interval end date time */
  private java.lang.CharSequence dateTimeEnd;
  /** Interval power value */
  private double power;
  /** Interval energy value */
  private double energy;
  /** Interval cost/income value */
  private double cost;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public TaskCalculationContractResultValueDto() {}

  /**
   * All-args constructor.
   * @param dateTimeStart Interval start date time
   * @param dateTimeEnd Interval end date time
   * @param power Interval power value
   * @param energy Interval energy value
   * @param cost Interval cost/income value
   */
  public TaskCalculationContractResultValueDto(java.lang.CharSequence dateTimeStart, java.lang.CharSequence dateTimeEnd, java.lang.Double power, java.lang.Double energy, java.lang.Double cost) {
    this.dateTimeStart = dateTimeStart;
    this.dateTimeEnd = dateTimeEnd;
    this.power = power;
    this.energy = energy;
    this.cost = cost;
  }

  @Override
  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }

  // Used by DatumWriter.  Applications should not call.
  @Override
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return dateTimeStart;
    case 1: return dateTimeEnd;
    case 2: return power;
    case 3: return energy;
    case 4: return cost;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @Override
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: dateTimeStart = (java.lang.CharSequence)value$; break;
    case 1: dateTimeEnd = (java.lang.CharSequence)value$; break;
    case 2: power = (java.lang.Double)value$; break;
    case 3: energy = (java.lang.Double)value$; break;
    case 4: cost = (java.lang.Double)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'dateTimeStart' field.
   * @return Interval start date time
   */
  public java.lang.CharSequence getDateTimeStart() {
    return dateTimeStart;
  }


  /**
   * Sets the value of the 'dateTimeStart' field.
   * Interval start date time
   * @param value the value to set.
   */
  public void setDateTimeStart(java.lang.CharSequence value) {
    this.dateTimeStart = value;
  }

  /**
   * Gets the value of the 'dateTimeEnd' field.
   * @return Interval end date time
   */
  public java.lang.CharSequence getDateTimeEnd() {
    return dateTimeEnd;
  }


  /**
   * Sets the value of the 'dateTimeEnd' field.
   * Interval end date time
   * @param value the value to set.
   */
  public void setDateTimeEnd(java.lang.CharSequence value) {
    this.dateTimeEnd = value;
  }

  /**
   * Gets the value of the 'power' field.
   * @return Interval power value
   */
  public double getPower() {
    return power;
  }


  /**
   * Sets the value of the 'power' field.
   * Interval power value
   * @param value the value to set.
   */
  public void setPower(double value) {
    this.power = value;
  }

  /**
   * Gets the value of the 'energy' field.
   * @return Interval energy value
   */
  public double getEnergy() {
    return energy;
  }


  /**
   * Sets the value of the 'energy' field.
   * Interval energy value
   * @param value the value to set.
   */
  public void setEnergy(double value) {
    this.energy = value;
  }

  /**
   * Gets the value of the 'cost' field.
   * @return Interval cost/income value
   */
  public double getCost() {
    return cost;
  }


  /**
   * Sets the value of the 'cost' field.
   * Interval cost/income value
   * @param value the value to set.
   */
  public void setCost(double value) {
    this.cost = value;
  }

  /**
   * Creates a new TaskCalculationContractResultValueDto RecordBuilder.
   * @return A new TaskCalculationContractResultValueDto RecordBuilder
   */
  public static com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto.Builder newBuilder() {
    return new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto.Builder();
  }

  /**
   * Creates a new TaskCalculationContractResultValueDto RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new TaskCalculationContractResultValueDto RecordBuilder
   */
  public static com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto.Builder newBuilder(com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto.Builder other) {
    if (other == null) {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto.Builder();
    } else {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto.Builder(other);
    }
  }

  /**
   * Creates a new TaskCalculationContractResultValueDto RecordBuilder by copying an existing TaskCalculationContractResultValueDto instance.
   * @param other The existing instance to copy.
   * @return A new TaskCalculationContractResultValueDto RecordBuilder
   */
  public static com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto.Builder newBuilder(com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto other) {
    if (other == null) {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto.Builder();
    } else {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto.Builder(other);
    }
  }

  /**
   * RecordBuilder for TaskCalculationContractResultValueDto instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<TaskCalculationContractResultValueDto>
    implements org.apache.avro.data.RecordBuilder<TaskCalculationContractResultValueDto> {

    /** Interval start date time */
    private java.lang.CharSequence dateTimeStart;
    /** Interval end date time */
    private java.lang.CharSequence dateTimeEnd;
    /** Interval power value */
    private double power;
    /** Interval energy value */
    private double energy;
    /** Interval cost/income value */
    private double cost;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.dateTimeStart)) {
        this.dateTimeStart = data().deepCopy(fields()[0].schema(), other.dateTimeStart);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.dateTimeEnd)) {
        this.dateTimeEnd = data().deepCopy(fields()[1].schema(), other.dateTimeEnd);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.power)) {
        this.power = data().deepCopy(fields()[2].schema(), other.power);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.energy)) {
        this.energy = data().deepCopy(fields()[3].schema(), other.energy);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
      if (isValidValue(fields()[4], other.cost)) {
        this.cost = data().deepCopy(fields()[4].schema(), other.cost);
        fieldSetFlags()[4] = other.fieldSetFlags()[4];
      }
    }

    /**
     * Creates a Builder by copying an existing TaskCalculationContractResultValueDto instance
     * @param other The existing instance to copy.
     */
    private Builder(com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.dateTimeStart)) {
        this.dateTimeStart = data().deepCopy(fields()[0].schema(), other.dateTimeStart);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.dateTimeEnd)) {
        this.dateTimeEnd = data().deepCopy(fields()[1].schema(), other.dateTimeEnd);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.power)) {
        this.power = data().deepCopy(fields()[2].schema(), other.power);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.energy)) {
        this.energy = data().deepCopy(fields()[3].schema(), other.energy);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.cost)) {
        this.cost = data().deepCopy(fields()[4].schema(), other.cost);
        fieldSetFlags()[4] = true;
      }
    }

    /**
      * Gets the value of the 'dateTimeStart' field.
      * Interval start date time
      * @return The value.
      */
    public java.lang.CharSequence getDateTimeStart() {
      return dateTimeStart;
    }


    /**
      * Sets the value of the 'dateTimeStart' field.
      * Interval start date time
      * @param value The value of 'dateTimeStart'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto.Builder setDateTimeStart(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.dateTimeStart = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'dateTimeStart' field has been set.
      * Interval start date time
      * @return True if the 'dateTimeStart' field has been set, false otherwise.
      */
    public boolean hasDateTimeStart() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'dateTimeStart' field.
      * Interval start date time
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto.Builder clearDateTimeStart() {
      dateTimeStart = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'dateTimeEnd' field.
      * Interval end date time
      * @return The value.
      */
    public java.lang.CharSequence getDateTimeEnd() {
      return dateTimeEnd;
    }


    /**
      * Sets the value of the 'dateTimeEnd' field.
      * Interval end date time
      * @param value The value of 'dateTimeEnd'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto.Builder setDateTimeEnd(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.dateTimeEnd = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'dateTimeEnd' field has been set.
      * Interval end date time
      * @return True if the 'dateTimeEnd' field has been set, false otherwise.
      */
    public boolean hasDateTimeEnd() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'dateTimeEnd' field.
      * Interval end date time
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto.Builder clearDateTimeEnd() {
      dateTimeEnd = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'power' field.
      * Interval power value
      * @return The value.
      */
    public double getPower() {
      return power;
    }


    /**
      * Sets the value of the 'power' field.
      * Interval power value
      * @param value The value of 'power'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto.Builder setPower(double value) {
      validate(fields()[2], value);
      this.power = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'power' field has been set.
      * Interval power value
      * @return True if the 'power' field has been set, false otherwise.
      */
    public boolean hasPower() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'power' field.
      * Interval power value
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto.Builder clearPower() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'energy' field.
      * Interval energy value
      * @return The value.
      */
    public double getEnergy() {
      return energy;
    }


    /**
      * Sets the value of the 'energy' field.
      * Interval energy value
      * @param value The value of 'energy'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto.Builder setEnergy(double value) {
      validate(fields()[3], value);
      this.energy = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'energy' field has been set.
      * Interval energy value
      * @return True if the 'energy' field has been set, false otherwise.
      */
    public boolean hasEnergy() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'energy' field.
      * Interval energy value
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto.Builder clearEnergy() {
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'cost' field.
      * Interval cost/income value
      * @return The value.
      */
    public double getCost() {
      return cost;
    }


    /**
      * Sets the value of the 'cost' field.
      * Interval cost/income value
      * @param value The value of 'cost'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto.Builder setCost(double value) {
      validate(fields()[4], value);
      this.cost = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'cost' field has been set.
      * Interval cost/income value
      * @return True if the 'cost' field has been set, false otherwise.
      */
    public boolean hasCost() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'cost' field.
      * Interval cost/income value
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto.Builder clearCost() {
      fieldSetFlags()[4] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TaskCalculationContractResultValueDto build() {
      try {
        TaskCalculationContractResultValueDto record = new TaskCalculationContractResultValueDto();
        record.dateTimeStart = fieldSetFlags()[0] ? this.dateTimeStart : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.dateTimeEnd = fieldSetFlags()[1] ? this.dateTimeEnd : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.power = fieldSetFlags()[2] ? this.power : (java.lang.Double) defaultValue(fields()[2]);
        record.energy = fieldSetFlags()[3] ? this.energy : (java.lang.Double) defaultValue(fields()[3]);
        record.cost = fieldSetFlags()[4] ? this.cost : (java.lang.Double) defaultValue(fields()[4]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<TaskCalculationContractResultValueDto>
    WRITER$ = (org.apache.avro.io.DatumWriter<TaskCalculationContractResultValueDto>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<TaskCalculationContractResultValueDto>
    READER$ = (org.apache.avro.io.DatumReader<TaskCalculationContractResultValueDto>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.dateTimeStart);

    out.writeString(this.dateTimeEnd);

    out.writeDouble(this.power);

    out.writeDouble(this.energy);

    out.writeDouble(this.cost);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.dateTimeStart = in.readString(this.dateTimeStart instanceof Utf8 ? (Utf8)this.dateTimeStart : null);

      this.dateTimeEnd = in.readString(this.dateTimeEnd instanceof Utf8 ? (Utf8)this.dateTimeEnd : null);

      this.power = in.readDouble();

      this.energy = in.readDouble();

      this.cost = in.readDouble();

    } else {
      for (int i = 0; i < 5; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.dateTimeStart = in.readString(this.dateTimeStart instanceof Utf8 ? (Utf8)this.dateTimeStart : null);
          break;

        case 1:
          this.dateTimeEnd = in.readString(this.dateTimeEnd instanceof Utf8 ? (Utf8)this.dateTimeEnd : null);
          break;

        case 2:
          this.power = in.readDouble();
          break;

        case 3:
          this.energy = in.readDouble();
          break;

        case 4:
          this.cost = in.readDouble();
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}











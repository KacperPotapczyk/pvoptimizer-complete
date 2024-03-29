/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class SumConstraintDto extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -4848024234099093106L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"SumConstraintDto\",\"namespace\":\"com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task\",\"fields\":[{\"name\":\"startInterval\",\"type\":\"int\"},{\"name\":\"endInterval\",\"type\":\"int\"},{\"name\":\"sum\",\"type\":\"double\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<SumConstraintDto> ENCODER =
      new BinaryMessageEncoder<>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<SumConstraintDto> DECODER =
      new BinaryMessageDecoder<>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<SumConstraintDto> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<SumConstraintDto> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<SumConstraintDto> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this SumConstraintDto to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a SumConstraintDto from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a SumConstraintDto instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static SumConstraintDto fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  private int startInterval;
  private int endInterval;
  private double sum;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public SumConstraintDto() {}

  /**
   * All-args constructor.
   * @param startInterval The new value for startInterval
   * @param endInterval The new value for endInterval
   * @param sum The new value for sum
   */
  public SumConstraintDto(java.lang.Integer startInterval, java.lang.Integer endInterval, java.lang.Double sum) {
    this.startInterval = startInterval;
    this.endInterval = endInterval;
    this.sum = sum;
  }

  @Override
  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }

  // Used by DatumWriter.  Applications should not call.
  @Override
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return startInterval;
    case 1: return endInterval;
    case 2: return sum;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @Override
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: startInterval = (java.lang.Integer)value$; break;
    case 1: endInterval = (java.lang.Integer)value$; break;
    case 2: sum = (java.lang.Double)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'startInterval' field.
   * @return The value of the 'startInterval' field.
   */
  public int getStartInterval() {
    return startInterval;
  }


  /**
   * Sets the value of the 'startInterval' field.
   * @param value the value to set.
   */
  public void setStartInterval(int value) {
    this.startInterval = value;
  }

  /**
   * Gets the value of the 'endInterval' field.
   * @return The value of the 'endInterval' field.
   */
  public int getEndInterval() {
    return endInterval;
  }


  /**
   * Sets the value of the 'endInterval' field.
   * @param value the value to set.
   */
  public void setEndInterval(int value) {
    this.endInterval = value;
  }

  /**
   * Gets the value of the 'sum' field.
   * @return The value of the 'sum' field.
   */
  public double getSum() {
    return sum;
  }


  /**
   * Sets the value of the 'sum' field.
   * @param value the value to set.
   */
  public void setSum(double value) {
    this.sum = value;
  }

  /**
   * Creates a new SumConstraintDto RecordBuilder.
   * @return A new SumConstraintDto RecordBuilder
   */
  public static com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.SumConstraintDto.Builder newBuilder() {
    return new com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.SumConstraintDto.Builder();
  }

  /**
   * Creates a new SumConstraintDto RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new SumConstraintDto RecordBuilder
   */
  public static com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.SumConstraintDto.Builder newBuilder(com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.SumConstraintDto.Builder other) {
    if (other == null) {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.SumConstraintDto.Builder();
    } else {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.SumConstraintDto.Builder(other);
    }
  }

  /**
   * Creates a new SumConstraintDto RecordBuilder by copying an existing SumConstraintDto instance.
   * @param other The existing instance to copy.
   * @return A new SumConstraintDto RecordBuilder
   */
  public static com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.SumConstraintDto.Builder newBuilder(com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.SumConstraintDto other) {
    if (other == null) {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.SumConstraintDto.Builder();
    } else {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.SumConstraintDto.Builder(other);
    }
  }

  /**
   * RecordBuilder for SumConstraintDto instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<SumConstraintDto>
    implements org.apache.avro.data.RecordBuilder<SumConstraintDto> {

    private int startInterval;
    private int endInterval;
    private double sum;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.SumConstraintDto.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.startInterval)) {
        this.startInterval = data().deepCopy(fields()[0].schema(), other.startInterval);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.endInterval)) {
        this.endInterval = data().deepCopy(fields()[1].schema(), other.endInterval);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.sum)) {
        this.sum = data().deepCopy(fields()[2].schema(), other.sum);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
    }

    /**
     * Creates a Builder by copying an existing SumConstraintDto instance
     * @param other The existing instance to copy.
     */
    private Builder(com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.SumConstraintDto other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.startInterval)) {
        this.startInterval = data().deepCopy(fields()[0].schema(), other.startInterval);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.endInterval)) {
        this.endInterval = data().deepCopy(fields()[1].schema(), other.endInterval);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.sum)) {
        this.sum = data().deepCopy(fields()[2].schema(), other.sum);
        fieldSetFlags()[2] = true;
      }
    }

    /**
      * Gets the value of the 'startInterval' field.
      * @return The value.
      */
    public int getStartInterval() {
      return startInterval;
    }


    /**
      * Sets the value of the 'startInterval' field.
      * @param value The value of 'startInterval'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.SumConstraintDto.Builder setStartInterval(int value) {
      validate(fields()[0], value);
      this.startInterval = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'startInterval' field has been set.
      * @return True if the 'startInterval' field has been set, false otherwise.
      */
    public boolean hasStartInterval() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'startInterval' field.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.SumConstraintDto.Builder clearStartInterval() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'endInterval' field.
      * @return The value.
      */
    public int getEndInterval() {
      return endInterval;
    }


    /**
      * Sets the value of the 'endInterval' field.
      * @param value The value of 'endInterval'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.SumConstraintDto.Builder setEndInterval(int value) {
      validate(fields()[1], value);
      this.endInterval = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'endInterval' field has been set.
      * @return True if the 'endInterval' field has been set, false otherwise.
      */
    public boolean hasEndInterval() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'endInterval' field.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.SumConstraintDto.Builder clearEndInterval() {
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'sum' field.
      * @return The value.
      */
    public double getSum() {
      return sum;
    }


    /**
      * Sets the value of the 'sum' field.
      * @param value The value of 'sum'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.SumConstraintDto.Builder setSum(double value) {
      validate(fields()[2], value);
      this.sum = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'sum' field has been set.
      * @return True if the 'sum' field has been set, false otherwise.
      */
    public boolean hasSum() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'sum' field.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.SumConstraintDto.Builder clearSum() {
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public SumConstraintDto build() {
      try {
        SumConstraintDto record = new SumConstraintDto();
        record.startInterval = fieldSetFlags()[0] ? this.startInterval : (java.lang.Integer) defaultValue(fields()[0]);
        record.endInterval = fieldSetFlags()[1] ? this.endInterval : (java.lang.Integer) defaultValue(fields()[1]);
        record.sum = fieldSetFlags()[2] ? this.sum : (java.lang.Double) defaultValue(fields()[2]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<SumConstraintDto>
    WRITER$ = (org.apache.avro.io.DatumWriter<SumConstraintDto>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<SumConstraintDto>
    READER$ = (org.apache.avro.io.DatumReader<SumConstraintDto>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeInt(this.startInterval);

    out.writeInt(this.endInterval);

    out.writeDouble(this.sum);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.startInterval = in.readInt();

      this.endInterval = in.readInt();

      this.sum = in.readDouble();

    } else {
      for (int i = 0; i < 3; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.startInterval = in.readInt();
          break;

        case 1:
          this.endInterval = in.readInt();
          break;

        case 2:
          this.sum = in.readDouble();
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}











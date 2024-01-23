/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class IntervalDto extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 6864447934219298099L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"IntervalDto\",\"namespace\":\"com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor\",\"fields\":[{\"name\":\"dateTimeStart\",\"type\":\"string\",\"doc\":\"Interval start date and time\"},{\"name\":\"dateTimeEnd\",\"type\":\"string\",\"doc\":\"Interval end date and time\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<IntervalDto> ENCODER =
      new BinaryMessageEncoder<>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<IntervalDto> DECODER =
      new BinaryMessageDecoder<>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<IntervalDto> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<IntervalDto> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<IntervalDto> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this IntervalDto to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a IntervalDto from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a IntervalDto instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static IntervalDto fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  /** Interval start date and time */
  private java.lang.CharSequence dateTimeStart;
  /** Interval end date and time */
  private java.lang.CharSequence dateTimeEnd;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public IntervalDto() {}

  /**
   * All-args constructor.
   * @param dateTimeStart Interval start date and time
   * @param dateTimeEnd Interval end date and time
   */
  public IntervalDto(java.lang.CharSequence dateTimeStart, java.lang.CharSequence dateTimeEnd) {
    this.dateTimeStart = dateTimeStart;
    this.dateTimeEnd = dateTimeEnd;
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
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'dateTimeStart' field.
   * @return Interval start date and time
   */
  public java.lang.CharSequence getDateTimeStart() {
    return dateTimeStart;
  }


  /**
   * Sets the value of the 'dateTimeStart' field.
   * Interval start date and time
   * @param value the value to set.
   */
  public void setDateTimeStart(java.lang.CharSequence value) {
    this.dateTimeStart = value;
  }

  /**
   * Gets the value of the 'dateTimeEnd' field.
   * @return Interval end date and time
   */
  public java.lang.CharSequence getDateTimeEnd() {
    return dateTimeEnd;
  }


  /**
   * Sets the value of the 'dateTimeEnd' field.
   * Interval end date and time
   * @param value the value to set.
   */
  public void setDateTimeEnd(java.lang.CharSequence value) {
    this.dateTimeEnd = value;
  }

  /**
   * Creates a new IntervalDto RecordBuilder.
   * @return A new IntervalDto RecordBuilder
   */
  public static com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.IntervalDto.Builder newBuilder() {
    return new com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.IntervalDto.Builder();
  }

  /**
   * Creates a new IntervalDto RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new IntervalDto RecordBuilder
   */
  public static com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.IntervalDto.Builder newBuilder(com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.IntervalDto.Builder other) {
    if (other == null) {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.IntervalDto.Builder();
    } else {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.IntervalDto.Builder(other);
    }
  }

  /**
   * Creates a new IntervalDto RecordBuilder by copying an existing IntervalDto instance.
   * @param other The existing instance to copy.
   * @return A new IntervalDto RecordBuilder
   */
  public static com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.IntervalDto.Builder newBuilder(com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.IntervalDto other) {
    if (other == null) {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.IntervalDto.Builder();
    } else {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.IntervalDto.Builder(other);
    }
  }

  /**
   * RecordBuilder for IntervalDto instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<IntervalDto>
    implements org.apache.avro.data.RecordBuilder<IntervalDto> {

    /** Interval start date and time */
    private java.lang.CharSequence dateTimeStart;
    /** Interval end date and time */
    private java.lang.CharSequence dateTimeEnd;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.IntervalDto.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.dateTimeStart)) {
        this.dateTimeStart = data().deepCopy(fields()[0].schema(), other.dateTimeStart);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.dateTimeEnd)) {
        this.dateTimeEnd = data().deepCopy(fields()[1].schema(), other.dateTimeEnd);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
    }

    /**
     * Creates a Builder by copying an existing IntervalDto instance
     * @param other The existing instance to copy.
     */
    private Builder(com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.IntervalDto other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.dateTimeStart)) {
        this.dateTimeStart = data().deepCopy(fields()[0].schema(), other.dateTimeStart);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.dateTimeEnd)) {
        this.dateTimeEnd = data().deepCopy(fields()[1].schema(), other.dateTimeEnd);
        fieldSetFlags()[1] = true;
      }
    }

    /**
      * Gets the value of the 'dateTimeStart' field.
      * Interval start date and time
      * @return The value.
      */
    public java.lang.CharSequence getDateTimeStart() {
      return dateTimeStart;
    }


    /**
      * Sets the value of the 'dateTimeStart' field.
      * Interval start date and time
      * @param value The value of 'dateTimeStart'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.IntervalDto.Builder setDateTimeStart(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.dateTimeStart = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'dateTimeStart' field has been set.
      * Interval start date and time
      * @return True if the 'dateTimeStart' field has been set, false otherwise.
      */
    public boolean hasDateTimeStart() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'dateTimeStart' field.
      * Interval start date and time
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.IntervalDto.Builder clearDateTimeStart() {
      dateTimeStart = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'dateTimeEnd' field.
      * Interval end date and time
      * @return The value.
      */
    public java.lang.CharSequence getDateTimeEnd() {
      return dateTimeEnd;
    }


    /**
      * Sets the value of the 'dateTimeEnd' field.
      * Interval end date and time
      * @param value The value of 'dateTimeEnd'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.IntervalDto.Builder setDateTimeEnd(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.dateTimeEnd = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'dateTimeEnd' field has been set.
      * Interval end date and time
      * @return True if the 'dateTimeEnd' field has been set, false otherwise.
      */
    public boolean hasDateTimeEnd() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'dateTimeEnd' field.
      * Interval end date and time
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.IntervalDto.Builder clearDateTimeEnd() {
      dateTimeEnd = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public IntervalDto build() {
      try {
        IntervalDto record = new IntervalDto();
        record.dateTimeStart = fieldSetFlags()[0] ? this.dateTimeStart : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.dateTimeEnd = fieldSetFlags()[1] ? this.dateTimeEnd : (java.lang.CharSequence) defaultValue(fields()[1]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<IntervalDto>
    WRITER$ = (org.apache.avro.io.DatumWriter<IntervalDto>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<IntervalDto>
    READER$ = (org.apache.avro.io.DatumReader<IntervalDto>)MODEL$.createDatumReader(SCHEMA$);

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

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.dateTimeStart = in.readString(this.dateTimeStart instanceof Utf8 ? (Utf8)this.dateTimeStart : null);

      this.dateTimeEnd = in.readString(this.dateTimeEnd instanceof Utf8 ? (Utf8)this.dateTimeEnd : null);

    } else {
      for (int i = 0; i < 2; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.dateTimeStart = in.readString(this.dateTimeStart instanceof Utf8 ? (Utf8)this.dateTimeStart : null);
          break;

        case 1:
          this.dateTimeEnd = in.readString(this.dateTimeEnd instanceof Utf8 ? (Utf8)this.dateTimeEnd : null);
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}











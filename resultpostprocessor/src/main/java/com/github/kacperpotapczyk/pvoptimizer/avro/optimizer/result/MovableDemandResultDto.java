/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.result;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class MovableDemandResultDto extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 7995293949553885284L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"MovableDemandResultDto\",\"namespace\":\"com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.result\",\"fields\":[{\"name\":\"id\",\"type\":\"long\",\"doc\":\"Movable demand id\"},{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Movable demand name\"},{\"name\":\"startInterval\",\"type\":\"int\",\"doc\":\"Optimal start interval\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<MovableDemandResultDto> ENCODER =
      new BinaryMessageEncoder<>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<MovableDemandResultDto> DECODER =
      new BinaryMessageDecoder<>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<MovableDemandResultDto> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<MovableDemandResultDto> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<MovableDemandResultDto> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this MovableDemandResultDto to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a MovableDemandResultDto from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a MovableDemandResultDto instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static MovableDemandResultDto fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  /** Movable demand id */
  private long id;
  /** Movable demand name */
  private java.lang.CharSequence name;
  /** Optimal start interval */
  private int startInterval;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public MovableDemandResultDto() {}

  /**
   * All-args constructor.
   * @param id Movable demand id
   * @param name Movable demand name
   * @param startInterval Optimal start interval
   */
  public MovableDemandResultDto(java.lang.Long id, java.lang.CharSequence name, java.lang.Integer startInterval) {
    this.id = id;
    this.name = name;
    this.startInterval = startInterval;
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
    case 1: return name;
    case 2: return startInterval;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @Override
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: id = (java.lang.Long)value$; break;
    case 1: name = (java.lang.CharSequence)value$; break;
    case 2: startInterval = (java.lang.Integer)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'id' field.
   * @return Movable demand id
   */
  public long getId() {
    return id;
  }


  /**
   * Sets the value of the 'id' field.
   * Movable demand id
   * @param value the value to set.
   */
  public void setId(long value) {
    this.id = value;
  }

  /**
   * Gets the value of the 'name' field.
   * @return Movable demand name
   */
  public java.lang.CharSequence getName() {
    return name;
  }


  /**
   * Sets the value of the 'name' field.
   * Movable demand name
   * @param value the value to set.
   */
  public void setName(java.lang.CharSequence value) {
    this.name = value;
  }

  /**
   * Gets the value of the 'startInterval' field.
   * @return Optimal start interval
   */
  public int getStartInterval() {
    return startInterval;
  }


  /**
   * Sets the value of the 'startInterval' field.
   * Optimal start interval
   * @param value the value to set.
   */
  public void setStartInterval(int value) {
    this.startInterval = value;
  }

  /**
   * Creates a new MovableDemandResultDto RecordBuilder.
   * @return A new MovableDemandResultDto RecordBuilder
   */
  public static com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.result.MovableDemandResultDto.Builder newBuilder() {
    return new com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.result.MovableDemandResultDto.Builder();
  }

  /**
   * Creates a new MovableDemandResultDto RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new MovableDemandResultDto RecordBuilder
   */
  public static com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.result.MovableDemandResultDto.Builder newBuilder(com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.result.MovableDemandResultDto.Builder other) {
    if (other == null) {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.result.MovableDemandResultDto.Builder();
    } else {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.result.MovableDemandResultDto.Builder(other);
    }
  }

  /**
   * Creates a new MovableDemandResultDto RecordBuilder by copying an existing MovableDemandResultDto instance.
   * @param other The existing instance to copy.
   * @return A new MovableDemandResultDto RecordBuilder
   */
  public static com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.result.MovableDemandResultDto.Builder newBuilder(com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.result.MovableDemandResultDto other) {
    if (other == null) {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.result.MovableDemandResultDto.Builder();
    } else {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.result.MovableDemandResultDto.Builder(other);
    }
  }

  /**
   * RecordBuilder for MovableDemandResultDto instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<MovableDemandResultDto>
    implements org.apache.avro.data.RecordBuilder<MovableDemandResultDto> {

    /** Movable demand id */
    private long id;
    /** Movable demand name */
    private java.lang.CharSequence name;
    /** Optimal start interval */
    private int startInterval;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.result.MovableDemandResultDto.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.name)) {
        this.name = data().deepCopy(fields()[1].schema(), other.name);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.startInterval)) {
        this.startInterval = data().deepCopy(fields()[2].schema(), other.startInterval);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
    }

    /**
     * Creates a Builder by copying an existing MovableDemandResultDto instance
     * @param other The existing instance to copy.
     */
    private Builder(com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.result.MovableDemandResultDto other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.name)) {
        this.name = data().deepCopy(fields()[1].schema(), other.name);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.startInterval)) {
        this.startInterval = data().deepCopy(fields()[2].schema(), other.startInterval);
        fieldSetFlags()[2] = true;
      }
    }

    /**
      * Gets the value of the 'id' field.
      * Movable demand id
      * @return The value.
      */
    public long getId() {
      return id;
    }


    /**
      * Sets the value of the 'id' field.
      * Movable demand id
      * @param value The value of 'id'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.result.MovableDemandResultDto.Builder setId(long value) {
      validate(fields()[0], value);
      this.id = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'id' field has been set.
      * Movable demand id
      * @return True if the 'id' field has been set, false otherwise.
      */
    public boolean hasId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'id' field.
      * Movable demand id
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.result.MovableDemandResultDto.Builder clearId() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'name' field.
      * Movable demand name
      * @return The value.
      */
    public java.lang.CharSequence getName() {
      return name;
    }


    /**
      * Sets the value of the 'name' field.
      * Movable demand name
      * @param value The value of 'name'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.result.MovableDemandResultDto.Builder setName(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.name = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'name' field has been set.
      * Movable demand name
      * @return True if the 'name' field has been set, false otherwise.
      */
    public boolean hasName() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'name' field.
      * Movable demand name
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.result.MovableDemandResultDto.Builder clearName() {
      name = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'startInterval' field.
      * Optimal start interval
      * @return The value.
      */
    public int getStartInterval() {
      return startInterval;
    }


    /**
      * Sets the value of the 'startInterval' field.
      * Optimal start interval
      * @param value The value of 'startInterval'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.result.MovableDemandResultDto.Builder setStartInterval(int value) {
      validate(fields()[2], value);
      this.startInterval = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'startInterval' field has been set.
      * Optimal start interval
      * @return True if the 'startInterval' field has been set, false otherwise.
      */
    public boolean hasStartInterval() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'startInterval' field.
      * Optimal start interval
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.result.MovableDemandResultDto.Builder clearStartInterval() {
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public MovableDemandResultDto build() {
      try {
        MovableDemandResultDto record = new MovableDemandResultDto();
        record.id = fieldSetFlags()[0] ? this.id : (java.lang.Long) defaultValue(fields()[0]);
        record.name = fieldSetFlags()[1] ? this.name : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.startInterval = fieldSetFlags()[2] ? this.startInterval : (java.lang.Integer) defaultValue(fields()[2]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<MovableDemandResultDto>
    WRITER$ = (org.apache.avro.io.DatumWriter<MovableDemandResultDto>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<MovableDemandResultDto>
    READER$ = (org.apache.avro.io.DatumReader<MovableDemandResultDto>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeLong(this.id);

    out.writeString(this.name);

    out.writeInt(this.startInterval);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.id = in.readLong();

      this.name = in.readString(this.name instanceof Utf8 ? (Utf8)this.name : null);

      this.startInterval = in.readInt();

    } else {
      for (int i = 0; i < 3; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.id = in.readLong();
          break;

        case 1:
          this.name = in.readString(this.name instanceof Utf8 ? (Utf8)this.name : null);
          break;

        case 2:
          this.startInterval = in.readInt();
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}











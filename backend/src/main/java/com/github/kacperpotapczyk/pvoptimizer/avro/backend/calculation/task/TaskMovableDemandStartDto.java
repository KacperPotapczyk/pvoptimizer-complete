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

/** Movable demand start option */
@org.apache.avro.specific.AvroGenerated
public class TaskMovableDemandStartDto extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 6541409062088843551L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"TaskMovableDemandStartDto\",\"namespace\":\"com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task\",\"doc\":\"Movable demand start option\",\"fields\":[{\"name\":\"start\",\"type\":\"string\",\"doc\":\"Start timestamp\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<TaskMovableDemandStartDto> ENCODER =
      new BinaryMessageEncoder<>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<TaskMovableDemandStartDto> DECODER =
      new BinaryMessageDecoder<>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<TaskMovableDemandStartDto> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<TaskMovableDemandStartDto> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<TaskMovableDemandStartDto> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this TaskMovableDemandStartDto to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a TaskMovableDemandStartDto from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a TaskMovableDemandStartDto instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static TaskMovableDemandStartDto fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  /** Start timestamp */
  private java.lang.CharSequence start;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public TaskMovableDemandStartDto() {}

  /**
   * All-args constructor.
   * @param start Start timestamp
   */
  public TaskMovableDemandStartDto(java.lang.CharSequence start) {
    this.start = start;
  }

  @Override
  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }

  // Used by DatumWriter.  Applications should not call.
  @Override
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return start;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @Override
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: start = (java.lang.CharSequence)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'start' field.
   * @return Start timestamp
   */
  public java.lang.CharSequence getStart() {
    return start;
  }


  /**
   * Sets the value of the 'start' field.
   * Start timestamp
   * @param value the value to set.
   */
  public void setStart(java.lang.CharSequence value) {
    this.start = value;
  }

  /**
   * Creates a new TaskMovableDemandStartDto RecordBuilder.
   * @return A new TaskMovableDemandStartDto RecordBuilder
   */
  public static com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto.Builder newBuilder() {
    return new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto.Builder();
  }

  /**
   * Creates a new TaskMovableDemandStartDto RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new TaskMovableDemandStartDto RecordBuilder
   */
  public static com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto.Builder newBuilder(com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto.Builder other) {
    if (other == null) {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto.Builder();
    } else {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto.Builder(other);
    }
  }

  /**
   * Creates a new TaskMovableDemandStartDto RecordBuilder by copying an existing TaskMovableDemandStartDto instance.
   * @param other The existing instance to copy.
   * @return A new TaskMovableDemandStartDto RecordBuilder
   */
  public static com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto.Builder newBuilder(com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto other) {
    if (other == null) {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto.Builder();
    } else {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto.Builder(other);
    }
  }

  /**
   * RecordBuilder for TaskMovableDemandStartDto instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<TaskMovableDemandStartDto>
    implements org.apache.avro.data.RecordBuilder<TaskMovableDemandStartDto> {

    /** Start timestamp */
    private java.lang.CharSequence start;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.start)) {
        this.start = data().deepCopy(fields()[0].schema(), other.start);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
    }

    /**
     * Creates a Builder by copying an existing TaskMovableDemandStartDto instance
     * @param other The existing instance to copy.
     */
    private Builder(com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.start)) {
        this.start = data().deepCopy(fields()[0].schema(), other.start);
        fieldSetFlags()[0] = true;
      }
    }

    /**
      * Gets the value of the 'start' field.
      * Start timestamp
      * @return The value.
      */
    public java.lang.CharSequence getStart() {
      return start;
    }


    /**
      * Sets the value of the 'start' field.
      * Start timestamp
      * @param value The value of 'start'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto.Builder setStart(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.start = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'start' field has been set.
      * Start timestamp
      * @return True if the 'start' field has been set, false otherwise.
      */
    public boolean hasStart() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'start' field.
      * Start timestamp
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto.Builder clearStart() {
      start = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TaskMovableDemandStartDto build() {
      try {
        TaskMovableDemandStartDto record = new TaskMovableDemandStartDto();
        record.start = fieldSetFlags()[0] ? this.start : (java.lang.CharSequence) defaultValue(fields()[0]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<TaskMovableDemandStartDto>
    WRITER$ = (org.apache.avro.io.DatumWriter<TaskMovableDemandStartDto>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<TaskMovableDemandStartDto>
    READER$ = (org.apache.avro.io.DatumReader<TaskMovableDemandStartDto>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.start);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.start = in.readString(this.start instanceof Utf8 ? (Utf8)this.start : null);

    } else {
      for (int i = 0; i < 1; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.start = in.readString(this.start instanceof Utf8 ? (Utf8)this.start : null);
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}











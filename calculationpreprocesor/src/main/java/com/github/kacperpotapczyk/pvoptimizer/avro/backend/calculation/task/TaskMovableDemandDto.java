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
public class TaskMovableDemandDto extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -7842311377702259892L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"TaskMovableDemandDto\",\"namespace\":\"com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task\",\"fields\":[{\"name\":\"id\",\"type\":\"long\",\"doc\":\"Movable demand Id\"},{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Movable demand name\"},{\"name\":\"revisionNumber\",\"type\":\"long\",\"doc\":\"Movable demand revision number\"},{\"name\":\"movableDemandStarts\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"TaskMovableDemandStartDto\",\"doc\":\"Movable demand start option\",\"fields\":[{\"name\":\"start\",\"type\":\"string\",\"doc\":\"Start timestamp\"}]}},\"doc\":\"Movable demand start options\",\"default\":[]},{\"name\":\"movableDemandValues\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"TaskMovableDemandValueDto\",\"doc\":\"Movable demand profile value\",\"fields\":[{\"name\":\"order\",\"type\":\"long\",\"doc\":\"Profile value ordering number\"},{\"name\":\"durationMinutes\",\"type\":\"long\",\"doc\":\"Profile value duration in minutes\"},{\"name\":\"value\",\"type\":\"double\",\"doc\":\"Demand value of profile\"}]}},\"doc\":\"Movable demand profile\",\"default\":[]}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<TaskMovableDemandDto> ENCODER =
      new BinaryMessageEncoder<>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<TaskMovableDemandDto> DECODER =
      new BinaryMessageDecoder<>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<TaskMovableDemandDto> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<TaskMovableDemandDto> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<TaskMovableDemandDto> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this TaskMovableDemandDto to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a TaskMovableDemandDto from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a TaskMovableDemandDto instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static TaskMovableDemandDto fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  /** Movable demand Id */
  private long id;
  /** Movable demand name */
  private java.lang.CharSequence name;
  /** Movable demand revision number */
  private long revisionNumber;
  /** Movable demand start options */
  private java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto> movableDemandStarts;
  /** Movable demand profile */
  private java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandValueDto> movableDemandValues;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public TaskMovableDemandDto() {}

  /**
   * All-args constructor.
   * @param id Movable demand Id
   * @param name Movable demand name
   * @param revisionNumber Movable demand revision number
   * @param movableDemandStarts Movable demand start options
   * @param movableDemandValues Movable demand profile
   */
  public TaskMovableDemandDto(java.lang.Long id, java.lang.CharSequence name, java.lang.Long revisionNumber, java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto> movableDemandStarts, java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandValueDto> movableDemandValues) {
    this.id = id;
    this.name = name;
    this.revisionNumber = revisionNumber;
    this.movableDemandStarts = movableDemandStarts;
    this.movableDemandValues = movableDemandValues;
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
    case 2: return revisionNumber;
    case 3: return movableDemandStarts;
    case 4: return movableDemandValues;
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
    case 2: revisionNumber = (java.lang.Long)value$; break;
    case 3: movableDemandStarts = (java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto>)value$; break;
    case 4: movableDemandValues = (java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandValueDto>)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'id' field.
   * @return Movable demand Id
   */
  public long getId() {
    return id;
  }


  /**
   * Sets the value of the 'id' field.
   * Movable demand Id
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
   * Gets the value of the 'revisionNumber' field.
   * @return Movable demand revision number
   */
  public long getRevisionNumber() {
    return revisionNumber;
  }


  /**
   * Sets the value of the 'revisionNumber' field.
   * Movable demand revision number
   * @param value the value to set.
   */
  public void setRevisionNumber(long value) {
    this.revisionNumber = value;
  }

  /**
   * Gets the value of the 'movableDemandStarts' field.
   * @return Movable demand start options
   */
  public java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto> getMovableDemandStarts() {
    return movableDemandStarts;
  }


  /**
   * Sets the value of the 'movableDemandStarts' field.
   * Movable demand start options
   * @param value the value to set.
   */
  public void setMovableDemandStarts(java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto> value) {
    this.movableDemandStarts = value;
  }

  /**
   * Gets the value of the 'movableDemandValues' field.
   * @return Movable demand profile
   */
  public java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandValueDto> getMovableDemandValues() {
    return movableDemandValues;
  }


  /**
   * Sets the value of the 'movableDemandValues' field.
   * Movable demand profile
   * @param value the value to set.
   */
  public void setMovableDemandValues(java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandValueDto> value) {
    this.movableDemandValues = value;
  }

  /**
   * Creates a new TaskMovableDemandDto RecordBuilder.
   * @return A new TaskMovableDemandDto RecordBuilder
   */
  public static com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandDto.Builder newBuilder() {
    return new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandDto.Builder();
  }

  /**
   * Creates a new TaskMovableDemandDto RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new TaskMovableDemandDto RecordBuilder
   */
  public static com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandDto.Builder newBuilder(com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandDto.Builder other) {
    if (other == null) {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandDto.Builder();
    } else {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandDto.Builder(other);
    }
  }

  /**
   * Creates a new TaskMovableDemandDto RecordBuilder by copying an existing TaskMovableDemandDto instance.
   * @param other The existing instance to copy.
   * @return A new TaskMovableDemandDto RecordBuilder
   */
  public static com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandDto.Builder newBuilder(com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandDto other) {
    if (other == null) {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandDto.Builder();
    } else {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandDto.Builder(other);
    }
  }

  /**
   * RecordBuilder for TaskMovableDemandDto instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<TaskMovableDemandDto>
    implements org.apache.avro.data.RecordBuilder<TaskMovableDemandDto> {

    /** Movable demand Id */
    private long id;
    /** Movable demand name */
    private java.lang.CharSequence name;
    /** Movable demand revision number */
    private long revisionNumber;
    /** Movable demand start options */
    private java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto> movableDemandStarts;
    /** Movable demand profile */
    private java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandValueDto> movableDemandValues;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandDto.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.name)) {
        this.name = data().deepCopy(fields()[1].schema(), other.name);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.revisionNumber)) {
        this.revisionNumber = data().deepCopy(fields()[2].schema(), other.revisionNumber);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.movableDemandStarts)) {
        this.movableDemandStarts = data().deepCopy(fields()[3].schema(), other.movableDemandStarts);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
      if (isValidValue(fields()[4], other.movableDemandValues)) {
        this.movableDemandValues = data().deepCopy(fields()[4].schema(), other.movableDemandValues);
        fieldSetFlags()[4] = other.fieldSetFlags()[4];
      }
    }

    /**
     * Creates a Builder by copying an existing TaskMovableDemandDto instance
     * @param other The existing instance to copy.
     */
    private Builder(com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandDto other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.name)) {
        this.name = data().deepCopy(fields()[1].schema(), other.name);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.revisionNumber)) {
        this.revisionNumber = data().deepCopy(fields()[2].schema(), other.revisionNumber);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.movableDemandStarts)) {
        this.movableDemandStarts = data().deepCopy(fields()[3].schema(), other.movableDemandStarts);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.movableDemandValues)) {
        this.movableDemandValues = data().deepCopy(fields()[4].schema(), other.movableDemandValues);
        fieldSetFlags()[4] = true;
      }
    }

    /**
      * Gets the value of the 'id' field.
      * Movable demand Id
      * @return The value.
      */
    public long getId() {
      return id;
    }


    /**
      * Sets the value of the 'id' field.
      * Movable demand Id
      * @param value The value of 'id'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandDto.Builder setId(long value) {
      validate(fields()[0], value);
      this.id = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'id' field has been set.
      * Movable demand Id
      * @return True if the 'id' field has been set, false otherwise.
      */
    public boolean hasId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'id' field.
      * Movable demand Id
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandDto.Builder clearId() {
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
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandDto.Builder setName(java.lang.CharSequence value) {
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
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandDto.Builder clearName() {
      name = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'revisionNumber' field.
      * Movable demand revision number
      * @return The value.
      */
    public long getRevisionNumber() {
      return revisionNumber;
    }


    /**
      * Sets the value of the 'revisionNumber' field.
      * Movable demand revision number
      * @param value The value of 'revisionNumber'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandDto.Builder setRevisionNumber(long value) {
      validate(fields()[2], value);
      this.revisionNumber = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'revisionNumber' field has been set.
      * Movable demand revision number
      * @return True if the 'revisionNumber' field has been set, false otherwise.
      */
    public boolean hasRevisionNumber() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'revisionNumber' field.
      * Movable demand revision number
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandDto.Builder clearRevisionNumber() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'movableDemandStarts' field.
      * Movable demand start options
      * @return The value.
      */
    public java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto> getMovableDemandStarts() {
      return movableDemandStarts;
    }


    /**
      * Sets the value of the 'movableDemandStarts' field.
      * Movable demand start options
      * @param value The value of 'movableDemandStarts'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandDto.Builder setMovableDemandStarts(java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto> value) {
      validate(fields()[3], value);
      this.movableDemandStarts = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'movableDemandStarts' field has been set.
      * Movable demand start options
      * @return True if the 'movableDemandStarts' field has been set, false otherwise.
      */
    public boolean hasMovableDemandStarts() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'movableDemandStarts' field.
      * Movable demand start options
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandDto.Builder clearMovableDemandStarts() {
      movableDemandStarts = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'movableDemandValues' field.
      * Movable demand profile
      * @return The value.
      */
    public java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandValueDto> getMovableDemandValues() {
      return movableDemandValues;
    }


    /**
      * Sets the value of the 'movableDemandValues' field.
      * Movable demand profile
      * @param value The value of 'movableDemandValues'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandDto.Builder setMovableDemandValues(java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandValueDto> value) {
      validate(fields()[4], value);
      this.movableDemandValues = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'movableDemandValues' field has been set.
      * Movable demand profile
      * @return True if the 'movableDemandValues' field has been set, false otherwise.
      */
    public boolean hasMovableDemandValues() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'movableDemandValues' field.
      * Movable demand profile
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandDto.Builder clearMovableDemandValues() {
      movableDemandValues = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TaskMovableDemandDto build() {
      try {
        TaskMovableDemandDto record = new TaskMovableDemandDto();
        record.id = fieldSetFlags()[0] ? this.id : (java.lang.Long) defaultValue(fields()[0]);
        record.name = fieldSetFlags()[1] ? this.name : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.revisionNumber = fieldSetFlags()[2] ? this.revisionNumber : (java.lang.Long) defaultValue(fields()[2]);
        record.movableDemandStarts = fieldSetFlags()[3] ? this.movableDemandStarts : (java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto>) defaultValue(fields()[3]);
        record.movableDemandValues = fieldSetFlags()[4] ? this.movableDemandValues : (java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandValueDto>) defaultValue(fields()[4]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<TaskMovableDemandDto>
    WRITER$ = (org.apache.avro.io.DatumWriter<TaskMovableDemandDto>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<TaskMovableDemandDto>
    READER$ = (org.apache.avro.io.DatumReader<TaskMovableDemandDto>)MODEL$.createDatumReader(SCHEMA$);

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

    out.writeLong(this.revisionNumber);

    long size0 = this.movableDemandStarts.size();
    out.writeArrayStart();
    out.setItemCount(size0);
    long actualSize0 = 0;
    for (com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto e0: this.movableDemandStarts) {
      actualSize0++;
      out.startItem();
      e0.customEncode(out);
    }
    out.writeArrayEnd();
    if (actualSize0 != size0)
      throw new java.util.ConcurrentModificationException("Array-size written was " + size0 + ", but element count was " + actualSize0 + ".");

    long size1 = this.movableDemandValues.size();
    out.writeArrayStart();
    out.setItemCount(size1);
    long actualSize1 = 0;
    for (com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandValueDto e1: this.movableDemandValues) {
      actualSize1++;
      out.startItem();
      e1.customEncode(out);
    }
    out.writeArrayEnd();
    if (actualSize1 != size1)
      throw new java.util.ConcurrentModificationException("Array-size written was " + size1 + ", but element count was " + actualSize1 + ".");

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.id = in.readLong();

      this.name = in.readString(this.name instanceof Utf8 ? (Utf8)this.name : null);

      this.revisionNumber = in.readLong();

      long size0 = in.readArrayStart();
      java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto> a0 = this.movableDemandStarts;
      if (a0 == null) {
        a0 = new SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto>((int)size0, SCHEMA$.getField("movableDemandStarts").schema());
        this.movableDemandStarts = a0;
      } else a0.clear();
      SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto> ga0 = (a0 instanceof SpecificData.Array ? (SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto>)a0 : null);
      for ( ; 0 < size0; size0 = in.arrayNext()) {
        for ( ; size0 != 0; size0--) {
          com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto e0 = (ga0 != null ? ga0.peek() : null);
          if (e0 == null) {
            e0 = new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto();
          }
          e0.customDecode(in);
          a0.add(e0);
        }
      }

      long size1 = in.readArrayStart();
      java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandValueDto> a1 = this.movableDemandValues;
      if (a1 == null) {
        a1 = new SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandValueDto>((int)size1, SCHEMA$.getField("movableDemandValues").schema());
        this.movableDemandValues = a1;
      } else a1.clear();
      SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandValueDto> ga1 = (a1 instanceof SpecificData.Array ? (SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandValueDto>)a1 : null);
      for ( ; 0 < size1; size1 = in.arrayNext()) {
        for ( ; size1 != 0; size1--) {
          com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandValueDto e1 = (ga1 != null ? ga1.peek() : null);
          if (e1 == null) {
            e1 = new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandValueDto();
          }
          e1.customDecode(in);
          a1.add(e1);
        }
      }

    } else {
      for (int i = 0; i < 5; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.id = in.readLong();
          break;

        case 1:
          this.name = in.readString(this.name instanceof Utf8 ? (Utf8)this.name : null);
          break;

        case 2:
          this.revisionNumber = in.readLong();
          break;

        case 3:
          long size0 = in.readArrayStart();
          java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto> a0 = this.movableDemandStarts;
          if (a0 == null) {
            a0 = new SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto>((int)size0, SCHEMA$.getField("movableDemandStarts").schema());
            this.movableDemandStarts = a0;
          } else a0.clear();
          SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto> ga0 = (a0 instanceof SpecificData.Array ? (SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto>)a0 : null);
          for ( ; 0 < size0; size0 = in.arrayNext()) {
            for ( ; size0 != 0; size0--) {
              com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto e0 = (ga0 != null ? ga0.peek() : null);
              if (e0 == null) {
                e0 = new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandStartDto();
              }
              e0.customDecode(in);
              a0.add(e0);
            }
          }
          break;

        case 4:
          long size1 = in.readArrayStart();
          java.util.List<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandValueDto> a1 = this.movableDemandValues;
          if (a1 == null) {
            a1 = new SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandValueDto>((int)size1, SCHEMA$.getField("movableDemandValues").schema());
            this.movableDemandValues = a1;
          } else a1.clear();
          SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandValueDto> ga1 = (a1 instanceof SpecificData.Array ? (SpecificData.Array<com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandValueDto>)a1 : null);
          for ( ; 0 < size1; size1 = in.arrayNext()) {
            for ( ; size1 != 0; size1--) {
              com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandValueDto e1 = (ga1 != null ? ga1.peek() : null);
              if (e1 == null) {
                e1 = new com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandValueDto();
              }
              e1.customDecode(in);
              a1.add(e1);
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










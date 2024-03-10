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

/** Movable demand data required for post processing of results */
@org.apache.avro.specific.AvroGenerated
public class MovableDemandPostProcessDto extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 947781037963341279L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"MovableDemandPostProcessDto\",\"namespace\":\"com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor\",\"doc\":\"Movable demand data required for post processing of results\",\"fields\":[{\"name\":\"id\",\"type\":\"long\",\"doc\":\"Movable demand identifier\"},{\"name\":\"name\",\"type\":\"string\",\"doc\":\"Movable demand name\"},{\"name\":\"demandPowerProfile\",\"type\":{\"type\":\"array\",\"items\":\"double\"},\"doc\":\"Profile of power demand\"},{\"name\":\"demandEnergyProfile\",\"type\":{\"type\":\"array\",\"items\":\"double\"},\"doc\":\"Profile of energy demand\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<MovableDemandPostProcessDto> ENCODER =
      new BinaryMessageEncoder<>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<MovableDemandPostProcessDto> DECODER =
      new BinaryMessageDecoder<>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<MovableDemandPostProcessDto> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<MovableDemandPostProcessDto> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<MovableDemandPostProcessDto> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this MovableDemandPostProcessDto to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a MovableDemandPostProcessDto from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a MovableDemandPostProcessDto instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static MovableDemandPostProcessDto fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  /** Movable demand identifier */
  private long id;
  /** Movable demand name */
  private java.lang.CharSequence name;
  /** Profile of power demand */
  private java.util.List<java.lang.Double> demandPowerProfile;
  /** Profile of energy demand */
  private java.util.List<java.lang.Double> demandEnergyProfile;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public MovableDemandPostProcessDto() {}

  /**
   * All-args constructor.
   * @param id Movable demand identifier
   * @param name Movable demand name
   * @param demandPowerProfile Profile of power demand
   * @param demandEnergyProfile Profile of energy demand
   */
  public MovableDemandPostProcessDto(java.lang.Long id, java.lang.CharSequence name, java.util.List<java.lang.Double> demandPowerProfile, java.util.List<java.lang.Double> demandEnergyProfile) {
    this.id = id;
    this.name = name;
    this.demandPowerProfile = demandPowerProfile;
    this.demandEnergyProfile = demandEnergyProfile;
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
    case 2: return demandPowerProfile;
    case 3: return demandEnergyProfile;
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
    case 2: demandPowerProfile = (java.util.List<java.lang.Double>)value$; break;
    case 3: demandEnergyProfile = (java.util.List<java.lang.Double>)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'id' field.
   * @return Movable demand identifier
   */
  public long getId() {
    return id;
  }


  /**
   * Sets the value of the 'id' field.
   * Movable demand identifier
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
   * Gets the value of the 'demandPowerProfile' field.
   * @return Profile of power demand
   */
  public java.util.List<java.lang.Double> getDemandPowerProfile() {
    return demandPowerProfile;
  }


  /**
   * Sets the value of the 'demandPowerProfile' field.
   * Profile of power demand
   * @param value the value to set.
   */
  public void setDemandPowerProfile(java.util.List<java.lang.Double> value) {
    this.demandPowerProfile = value;
  }

  /**
   * Gets the value of the 'demandEnergyProfile' field.
   * @return Profile of energy demand
   */
  public java.util.List<java.lang.Double> getDemandEnergyProfile() {
    return demandEnergyProfile;
  }


  /**
   * Sets the value of the 'demandEnergyProfile' field.
   * Profile of energy demand
   * @param value the value to set.
   */
  public void setDemandEnergyProfile(java.util.List<java.lang.Double> value) {
    this.demandEnergyProfile = value;
  }

  /**
   * Creates a new MovableDemandPostProcessDto RecordBuilder.
   * @return A new MovableDemandPostProcessDto RecordBuilder
   */
  public static com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.MovableDemandPostProcessDto.Builder newBuilder() {
    return new com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.MovableDemandPostProcessDto.Builder();
  }

  /**
   * Creates a new MovableDemandPostProcessDto RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new MovableDemandPostProcessDto RecordBuilder
   */
  public static com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.MovableDemandPostProcessDto.Builder newBuilder(com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.MovableDemandPostProcessDto.Builder other) {
    if (other == null) {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.MovableDemandPostProcessDto.Builder();
    } else {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.MovableDemandPostProcessDto.Builder(other);
    }
  }

  /**
   * Creates a new MovableDemandPostProcessDto RecordBuilder by copying an existing MovableDemandPostProcessDto instance.
   * @param other The existing instance to copy.
   * @return A new MovableDemandPostProcessDto RecordBuilder
   */
  public static com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.MovableDemandPostProcessDto.Builder newBuilder(com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.MovableDemandPostProcessDto other) {
    if (other == null) {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.MovableDemandPostProcessDto.Builder();
    } else {
      return new com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.MovableDemandPostProcessDto.Builder(other);
    }
  }

  /**
   * RecordBuilder for MovableDemandPostProcessDto instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<MovableDemandPostProcessDto>
    implements org.apache.avro.data.RecordBuilder<MovableDemandPostProcessDto> {

    /** Movable demand identifier */
    private long id;
    /** Movable demand name */
    private java.lang.CharSequence name;
    /** Profile of power demand */
    private java.util.List<java.lang.Double> demandPowerProfile;
    /** Profile of energy demand */
    private java.util.List<java.lang.Double> demandEnergyProfile;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.MovableDemandPostProcessDto.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.name)) {
        this.name = data().deepCopy(fields()[1].schema(), other.name);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.demandPowerProfile)) {
        this.demandPowerProfile = data().deepCopy(fields()[2].schema(), other.demandPowerProfile);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.demandEnergyProfile)) {
        this.demandEnergyProfile = data().deepCopy(fields()[3].schema(), other.demandEnergyProfile);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
    }

    /**
     * Creates a Builder by copying an existing MovableDemandPostProcessDto instance
     * @param other The existing instance to copy.
     */
    private Builder(com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.MovableDemandPostProcessDto other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.name)) {
        this.name = data().deepCopy(fields()[1].schema(), other.name);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.demandPowerProfile)) {
        this.demandPowerProfile = data().deepCopy(fields()[2].schema(), other.demandPowerProfile);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.demandEnergyProfile)) {
        this.demandEnergyProfile = data().deepCopy(fields()[3].schema(), other.demandEnergyProfile);
        fieldSetFlags()[3] = true;
      }
    }

    /**
      * Gets the value of the 'id' field.
      * Movable demand identifier
      * @return The value.
      */
    public long getId() {
      return id;
    }


    /**
      * Sets the value of the 'id' field.
      * Movable demand identifier
      * @param value The value of 'id'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.MovableDemandPostProcessDto.Builder setId(long value) {
      validate(fields()[0], value);
      this.id = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'id' field has been set.
      * Movable demand identifier
      * @return True if the 'id' field has been set, false otherwise.
      */
    public boolean hasId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'id' field.
      * Movable demand identifier
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.MovableDemandPostProcessDto.Builder clearId() {
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
    public com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.MovableDemandPostProcessDto.Builder setName(java.lang.CharSequence value) {
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
    public com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.MovableDemandPostProcessDto.Builder clearName() {
      name = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'demandPowerProfile' field.
      * Profile of power demand
      * @return The value.
      */
    public java.util.List<java.lang.Double> getDemandPowerProfile() {
      return demandPowerProfile;
    }


    /**
      * Sets the value of the 'demandPowerProfile' field.
      * Profile of power demand
      * @param value The value of 'demandPowerProfile'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.MovableDemandPostProcessDto.Builder setDemandPowerProfile(java.util.List<java.lang.Double> value) {
      validate(fields()[2], value);
      this.demandPowerProfile = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'demandPowerProfile' field has been set.
      * Profile of power demand
      * @return True if the 'demandPowerProfile' field has been set, false otherwise.
      */
    public boolean hasDemandPowerProfile() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'demandPowerProfile' field.
      * Profile of power demand
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.MovableDemandPostProcessDto.Builder clearDemandPowerProfile() {
      demandPowerProfile = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'demandEnergyProfile' field.
      * Profile of energy demand
      * @return The value.
      */
    public java.util.List<java.lang.Double> getDemandEnergyProfile() {
      return demandEnergyProfile;
    }


    /**
      * Sets the value of the 'demandEnergyProfile' field.
      * Profile of energy demand
      * @param value The value of 'demandEnergyProfile'.
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.MovableDemandPostProcessDto.Builder setDemandEnergyProfile(java.util.List<java.lang.Double> value) {
      validate(fields()[3], value);
      this.demandEnergyProfile = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'demandEnergyProfile' field has been set.
      * Profile of energy demand
      * @return True if the 'demandEnergyProfile' field has been set, false otherwise.
      */
    public boolean hasDemandEnergyProfile() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'demandEnergyProfile' field.
      * Profile of energy demand
      * @return This builder.
      */
    public com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.MovableDemandPostProcessDto.Builder clearDemandEnergyProfile() {
      demandEnergyProfile = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public MovableDemandPostProcessDto build() {
      try {
        MovableDemandPostProcessDto record = new MovableDemandPostProcessDto();
        record.id = fieldSetFlags()[0] ? this.id : (java.lang.Long) defaultValue(fields()[0]);
        record.name = fieldSetFlags()[1] ? this.name : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.demandPowerProfile = fieldSetFlags()[2] ? this.demandPowerProfile : (java.util.List<java.lang.Double>) defaultValue(fields()[2]);
        record.demandEnergyProfile = fieldSetFlags()[3] ? this.demandEnergyProfile : (java.util.List<java.lang.Double>) defaultValue(fields()[3]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<MovableDemandPostProcessDto>
    WRITER$ = (org.apache.avro.io.DatumWriter<MovableDemandPostProcessDto>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<MovableDemandPostProcessDto>
    READER$ = (org.apache.avro.io.DatumReader<MovableDemandPostProcessDto>)MODEL$.createDatumReader(SCHEMA$);

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

    long size0 = this.demandPowerProfile.size();
    out.writeArrayStart();
    out.setItemCount(size0);
    long actualSize0 = 0;
    for (java.lang.Double e0: this.demandPowerProfile) {
      actualSize0++;
      out.startItem();
      out.writeDouble(e0);
    }
    out.writeArrayEnd();
    if (actualSize0 != size0)
      throw new java.util.ConcurrentModificationException("Array-size written was " + size0 + ", but element count was " + actualSize0 + ".");

    long size1 = this.demandEnergyProfile.size();
    out.writeArrayStart();
    out.setItemCount(size1);
    long actualSize1 = 0;
    for (java.lang.Double e1: this.demandEnergyProfile) {
      actualSize1++;
      out.startItem();
      out.writeDouble(e1);
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

      long size0 = in.readArrayStart();
      java.util.List<java.lang.Double> a0 = this.demandPowerProfile;
      if (a0 == null) {
        a0 = new SpecificData.Array<java.lang.Double>((int)size0, SCHEMA$.getField("demandPowerProfile").schema());
        this.demandPowerProfile = a0;
      } else a0.clear();
      SpecificData.Array<java.lang.Double> ga0 = (a0 instanceof SpecificData.Array ? (SpecificData.Array<java.lang.Double>)a0 : null);
      for ( ; 0 < size0; size0 = in.arrayNext()) {
        for ( ; size0 != 0; size0--) {
          java.lang.Double e0 = (ga0 != null ? ga0.peek() : null);
          e0 = in.readDouble();
          a0.add(e0);
        }
      }

      long size1 = in.readArrayStart();
      java.util.List<java.lang.Double> a1 = this.demandEnergyProfile;
      if (a1 == null) {
        a1 = new SpecificData.Array<java.lang.Double>((int)size1, SCHEMA$.getField("demandEnergyProfile").schema());
        this.demandEnergyProfile = a1;
      } else a1.clear();
      SpecificData.Array<java.lang.Double> ga1 = (a1 instanceof SpecificData.Array ? (SpecificData.Array<java.lang.Double>)a1 : null);
      for ( ; 0 < size1; size1 = in.arrayNext()) {
        for ( ; size1 != 0; size1--) {
          java.lang.Double e1 = (ga1 != null ? ga1.peek() : null);
          e1 = in.readDouble();
          a1.add(e1);
        }
      }

    } else {
      for (int i = 0; i < 4; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.id = in.readLong();
          break;

        case 1:
          this.name = in.readString(this.name instanceof Utf8 ? (Utf8)this.name : null);
          break;

        case 2:
          long size0 = in.readArrayStart();
          java.util.List<java.lang.Double> a0 = this.demandPowerProfile;
          if (a0 == null) {
            a0 = new SpecificData.Array<java.lang.Double>((int)size0, SCHEMA$.getField("demandPowerProfile").schema());
            this.demandPowerProfile = a0;
          } else a0.clear();
          SpecificData.Array<java.lang.Double> ga0 = (a0 instanceof SpecificData.Array ? (SpecificData.Array<java.lang.Double>)a0 : null);
          for ( ; 0 < size0; size0 = in.arrayNext()) {
            for ( ; size0 != 0; size0--) {
              java.lang.Double e0 = (ga0 != null ? ga0.peek() : null);
              e0 = in.readDouble();
              a0.add(e0);
            }
          }
          break;

        case 3:
          long size1 = in.readArrayStart();
          java.util.List<java.lang.Double> a1 = this.demandEnergyProfile;
          if (a1 == null) {
            a1 = new SpecificData.Array<java.lang.Double>((int)size1, SCHEMA$.getField("demandEnergyProfile").schema());
            this.demandEnergyProfile = a1;
          } else a1.clear();
          SpecificData.Array<java.lang.Double> ga1 = (a1 instanceof SpecificData.Array ? (SpecificData.Array<java.lang.Double>)a1 : null);
          for ( ; 0 < size1; size1 = in.arrayNext()) {
            for ( ; size1 != 0; size1--) {
              java.lang.Double e1 = (ga1 != null ? ga1.peek() : null);
              e1 = in.readDouble();
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










// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: MessageId.proto

package info.xiaomo.server.shared.protocol.msg;

/**
 * Protobuf enum {@code UserMsgId}
 */
public enum UserMsgId
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>UserUnknown = 0;</code>
   */
  UserUnknown(0),
  /**
   * <code>LoginRequest = 101001;</code>
   */
  LoginRequest(101001),
  /**
   * <code>LoginResponse = 101002;</code>
   */
  LoginResponse(101002),
  UNRECOGNIZED(-1),
  ;

  /**
   * <code>UserUnknown = 0;</code>
   */
  public static final int UserUnknown_VALUE = 0;
  /**
   * <code>LoginRequest = 101001;</code>
   */
  public static final int LoginRequest_VALUE = 101001;
  /**
   * <code>LoginResponse = 101002;</code>
   */
  public static final int LoginResponse_VALUE = 101002;


  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @Deprecated
  public static UserMsgId valueOf(int value) {
    return forNumber(value);
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   */
  public static UserMsgId forNumber(int value) {
    switch (value) {
      case 0: return UserUnknown;
      case 101001: return LoginRequest;
      case 101002: return LoginResponse;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<UserMsgId>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      UserMsgId> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<UserMsgId>() {
          public UserMsgId findValueByNumber(int number) {
            return UserMsgId.forNumber(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    if (this == UNRECOGNIZED) {
      throw new IllegalStateException(
          "Can't get the descriptor of an unrecognized enum value.");
    }
    return getDescriptor().getValues().get(ordinal());
  }
  public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
    return MessageId.getDescriptor().getEnumTypes().get(1);
  }

  private static final UserMsgId[] VALUES = values();

  public static UserMsgId valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    if (desc.getIndex() == -1) {
      return UNRECOGNIZED;
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private UserMsgId(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:UserMsgId)
}


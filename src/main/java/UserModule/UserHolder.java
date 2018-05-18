package UserModule;

/**
* UserModule/UserHolder.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从user.idl
* 2018年5月8日 星期二 下午03时50分23秒 CST
*/

public final class UserHolder implements org.omg.CORBA.portable.Streamable
{
  public UserModule.User value = null;

  public UserHolder ()
  {
  }

  public UserHolder (UserModule.User initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = UserModule.UserHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    UserModule.UserHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return UserModule.UserHelper.type ();
  }

}

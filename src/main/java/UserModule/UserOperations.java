package UserModule;


/**
* UserModule/UserOperations.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��user.idl
* 2018��5��8�� ���ڶ� ����03ʱ50��23�� CST
*/

public interface UserOperations 
{
  boolean add (String startTime, String endTime, String label);
  String query (String startTime, String endTime);
  boolean delete (String key);
  boolean clear ();
  String show ();
} // interface UserOperations

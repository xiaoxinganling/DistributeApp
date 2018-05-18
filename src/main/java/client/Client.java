package client;

import CreatorModule.Creator;
import CreatorModule.CreatorHelper;
import UserModule.User;
import UserModule.UserHelper;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.regex.Pattern;

public class Client {
    private Creator creator;
    private User user;
    private BufferedReader reader;
    private ORB orb;
    private org.omg.CORBA.Object objRef;
    private NamingContextExt ncRef;

    public Client()
    {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }
    private void init()
    {
        System.out.println("Client init config starts....");
        String[] args = {};
        Properties properties = new Properties();
        //指定ORB的ip地址
        properties.put("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
        //指定ORB的端口
        properties.put("org.omg.CORBA.ORBInitialPort", "8080");

        //创建一个ORB实例
        orb = ORB.init(args, properties);

        //获取根名称上下文
        try {
            objRef = orb.resolve_initial_references("NameService");
        } catch (InvalidName e) {
            e.printStackTrace();
        }
        ncRef = NamingContextExtHelper.narrow(objRef);

        String name = "Creator";
        try {
            //通过ORB拿到server实例化好的Creator类
            creator = CreatorHelper.narrow(ncRef.resolve_str(name));
        } catch (NotFound e) {
            e.printStackTrace();
        } catch (CannotProceed e) {
            e.printStackTrace();
        } catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
            e.printStackTrace();
        }

        System.out.println("Client init config ends...");
    }

    //与用户交互
    public void procedure() {
        String choice;
        String startTime, endTime, label;
        String index;

        try {
            while (true) {
                System.out.println("Welcome to to_do_list_Client!Please choose:");
                System.out.println("1.Register\n2.Login\n3.Exit");
                choice = reader.readLine();
                if(choice.equals("1"))
                {
                    register();
                }
                else if(choice.equals("2"))
                {
                    if(login())
                    {
                        System.out.println("Login Successful!");
                        while(true)
                        {
                            System.out.println("Please choose following command:");
                            System.out.println("1.Add item\n" +
                                    "2.Query item\n" +
                                    "3.Show items\n" +
                                    "4.Delete item\n" +
                                    "5.Clear items\n" +
                                    "6.Logout");
                            choice = reader.readLine();
                            if(choice.equals("1"))
                            {
                                System.out.println("please input startTime (like this:2017-04-19,08:20):");
                                startTime = reader.readLine();
                                System.out.println("please input endTime (like this:2017-04-19,08:20):");
                                endTime = reader.readLine();
                                System.out.println("please input label:");
                                label = reader.readLine();
                                if (user.add(startTime, endTime, label)) {
                                    System.out.println("Add item successful!");
                                } else {
                                    System.out.println("Add item fail!");
                                }
                            }
                            else if(choice.equals("2"))
                            {
                                System.out.println("please input startTime (like this:2017-04-19,08:20):");
                                startTime = reader.readLine();
                                System.out.println("please input endTime (like this:2017-04-19,08:20):");
                                endTime = reader.readLine();
                                System.out.println(user.query(startTime, endTime));
                            }
                            else if(choice.equals("3"))
                            {
                                System.out.println(user.show());
                            }
                            else if(choice.equals("4"))
                            {
                                System.out.println("please input index:");
                                index = reader.readLine();
                                Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
                                if (pattern.matcher(index).matches())
                                {
                                    if (user.delete(index)) {
                                        System.out.println("Delete item successful!");
                                    } else {
                                        System.out.println("Delete item fail!");
                                    }
                                }
                                else
                                {
                                    System.out.println("Wrong Index!");
                                }
                            }
                            else if(choice.equals("5"))
                            {
                                if (user.clear()) {
                                    System.out.println("Clear items done!");
                                }
                                else
                                {
                                    System.out.println("Clear items fail!");
                                }
                            }
                            else if(choice.equals("6"))
                            {
                                break;
                            }
                        }
                    }
                    else
                    {
                        System.out.println("Login fail!");
                    }
                }
                else if(choice.equals("3"))
                {
                    return;
                }
                else
                {
                    System.out.println("Wrong Input!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //注册
    private boolean register() {
        String username, password;
        try {
            System.out.println("please input username:");
            username = reader.readLine();
            System.out.println("please input password:");
            password = reader.readLine();
            if (creator.register(username, password)) {
                System.out.println("Register successful!");
                return true;
            } else {
                System.out.println("Register fail!");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //登录
    private boolean login() {
        String username, password;

        try {
            System.out.println("please input username:");
            username = reader.readLine();
            System.out.println("please input password:");
            password = reader.readLine();
            if (creator.login(username, password)) {
                try {
                    //通过ORB拿到server实例化好的User类
                    user = UserHelper.narrow(ncRef.resolve_str(username));
                } catch (NotFound e) {
                    e.printStackTrace();
                } catch (CannotProceed e) {
                    e.printStackTrace();
                } catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
                    e.printStackTrace();
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void main(String[] args) {
        Client client = new Client();
        client.init();
        client.procedure();
    }
}

package zzl.sql;

import zzl.beans.BookInfo;
import zzl.beans.User;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class MySql {
    private final static String DATA_URL = "jdbc:mysql://localhost:3306/web_schema?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";
    private final static String DATABASE_NAME = "zzl";
    private final static String DATABASE_PWD = "Zzl123456";
    private final static String JDBC_NAME = "com.mysql.cj.jdbc.Driver";
    public final static String REGISTER_SQL = "insert into user (email,pwd,u_name) values (?,?,?)";
    private final static String INSERT_BOOK_SQL = "insert into books (name,author,covers,pub_house,description,u_id) values (?,?,?,?,?,?)";
    private final static String UPDATE_BOOK_SQL = "update books set name=?,author=?,covers=?,pub_house=?,description=?,u_id=? where idbooks=?";
    private final static String CHECK_EMAIL_SQL = "SELECT email from user where email=?";
    private final static String GET_BOOK_LIST_SQL = "select idbooks,name,author,covers from books";
    private final static String GET_BOOK_SQL = "select name,author,covers,pub_house,description,u_name,user.u_id,permission from books,user where books.u_id=user.u_id and idbooks=?";

    public static Connection getConnection() {
        /*
        try {
            Class.forName(JDBC_NAME);//加载数据库驱动
            return DriverManager.getConnection(DATA_URL, DATABASE_NAME, DATABASE_PWD);
        } catch (ClassNotFoundException|SQLException e) {
            e.printStackTrace();
        }
        return null;*/
        try {
            Context initContext = new InitialContext();
            // 找到DataSource
            DataSource ds = (DataSource) initContext.lookup("java:comp/env/jdbc");
            return ds.getConnection();
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从数据库中获取数据
     *
     * @param sql
     * @param listener 数据处理接口
     * @return
     */
    public static Object getData(String sql, OnResultSetHandler listener) {
        PreparedStatement psm = null;
        Connection connection = null;
        try {
            connection = getConnection();
            psm = connection.prepareStatement(sql);
            listener.setPreparedStatement(psm);
            ResultSet rs = psm.executeQuery();
            return listener.handleResult(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(psm, connection);
        }
        return null;
    }

    /**
     * 检验邮箱是否注册
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {

        return (boolean) getData(CHECK_EMAIL_SQL, new OnResultSetHandler() {
            @Override
            public void setPreparedStatement(PreparedStatement ps) {
                try {
                    ps.setString(1, email);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public Object handleResult(ResultSet rs) {
                try {
                    return !rs.next();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    /**
     * 得到图书信息
     *
     * @return
     */
    public static List<BookInfo> getBooks() {

        final List<BookInfo> list = new LinkedList<>();
        getData(GET_BOOK_LIST_SQL, new OnResultSetHandler() {
            @Override
            public void setPreparedStatement(PreparedStatement ps) {
            }
            @Override
            public Object handleResult(ResultSet rs) {
                try {
                    while (rs.next()) {
                        BookInfo bookInfo = new BookInfo();
                        bookInfo.setId(rs.getInt("idbooks"));
                        bookInfo.setName(rs.getString("name"));
                        bookInfo.setAuthor(rs.getString("author"));
                        bookInfo.setCovers(rs.getString("covers"));
                        list.add(bookInfo);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        return list;
    }
    public static boolean deleteBook(int bookID,int userID,boolean isAdmin)
    {
        PreparedStatement psm = null;
        Connection connection = null;

        try {
            connection = getConnection();
            String sql="delete from books where idbooks=? and (u_id=? or ?)";
            psm = connection.prepareStatement(sql);
            psm.setInt(1,bookID);
            psm.setInt(2,userID);
            psm.setBoolean(3,isAdmin);
            return psm.executeUpdate()==1;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(psm, connection);
        }
        return false;
    }
    public static List<BookInfo> getUserShareBooks(int userID)
    {
        List<BookInfo> list=new LinkedList<>();
        String sql="select name,author,pub_house,idbooks from books where u_id=?";
        getData(sql, new OnResultSetHandler() {
            @Override
            public void setPreparedStatement(PreparedStatement ps) {
                try {
                    ps.setInt(1,userID);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public Object handleResult(ResultSet rs) {
                try {
                    while (rs.next())
                    {
                        BookInfo book=new BookInfo();
                        book.setName(rs.getString("name"));
                        book.setAuthor(rs.getString("author"));
                        book.setPublishingHouse(rs.getString("pub_house"));
                        book.setId(rs.getInt("idbooks"));
                        list.add(book);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        return list;
    }
    public static BookInfo getBookInfo(String id) {

        return (BookInfo) getData(GET_BOOK_SQL, new OnResultSetHandler() {
            @Override
            public void setPreparedStatement(PreparedStatement ps) {
                try {
                    ps.setString(1, id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public Object handleResult(ResultSet rs) {
                try {
                    if (rs.next()) {
                        BookInfo bookInfo = new BookInfo();
                        bookInfo.setName(rs.getString("name"));
                        bookInfo.setAuthor(rs.getString("author"));
                        bookInfo.setCovers(rs.getString("covers"));
                        bookInfo.setPublishingHouse(rs.getString("pub_house"));
                        bookInfo.setDescription(rs.getString("description"));
                        User user = new User();
                        user.setName(rs.getString("u_name"));
                        user.setId(rs.getInt("u_id"));
                        user.setPermission(rs.getString("permission"));
                        bookInfo.setUser(user);
                        return bookInfo;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    /**
     * 登录验证
     *
     * @param email
     * @param pwd
     * @return
     */
    public static User login(final String email, final String pwd) {
        String sql = "SELECT pwd,u_id,u_name,permission from user where email=?";
        return (User) getData(sql, new OnResultSetHandler() {
            @Override
            public void setPreparedStatement(PreparedStatement ps) {
                try {
                    ps.setString(1, email);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public Object handleResult(ResultSet rs) {
                try {
                    if (rs.next()) {
                        if (rs.getString("pwd").equals(pwd)) ;
                        {
                            User user = new User();
                            user.setEmail(email);
                            user.setId(rs.getInt("u_id"));
                            user.setName(rs.getString("u_name"));
                            user.setPermission(rs.getString("permission"));
                            return user;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

    }

    /**
     * @param bookInfo
     * @param id
     * @param bookID   bookID<0时为插入
     * @return
     */
    public static int updateBook(BookInfo bookInfo, int id, int bookID) {
        int enterInfoId = -1;
        PreparedStatement psm = null;
        Connection connection = null;

        try {
            connection = getConnection();
            assert connection != null;
            if (bookID <= 0) {
                psm = connection.prepareStatement(INSERT_BOOK_SQL, Statement.RETURN_GENERATED_KEYS);
            } else {
                psm = connection.prepareStatement(UPDATE_BOOK_SQL);
            }
            psm.setString(1, bookInfo.getName());
            psm.setString(2, bookInfo.getAuthor());
            psm.setString(3, bookInfo.getCovers());
            psm.setString(4, bookInfo.getPublishingHouse());
            psm.setString(5, bookInfo.getDescription());
            psm.setInt(6, id);
            if (bookID > 0)
                psm.setInt(7, bookID);
            psm.executeUpdate();
            if(bookID<=0)
            {
                ResultSet rst = psm.getGeneratedKeys();
                if (rst.next()) {
                    enterInfoId = rst.getInt(1);
                }
            }else
                enterInfoId=bookID;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(psm, connection);
        }
        return enterInfoId;
    }

    /**
     * 插入数据
     *
     * @param sql
     * @param values
     */
    public static int insertData(String sql, String[] values) {
        PreparedStatement psm = null;
        Connection connection = null;
        try {
            connection = getConnection();
            assert connection != null;
            psm = connection.prepareStatement(sql);
            for (int i = 0; i < values.length; i++) {
                psm.setString(i + 1, values[i]);
            }
            return psm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(psm, connection);
        }
        return 0;
    }

    private static void closeConnection(PreparedStatement psm, Connection connection) {
        try {
            if (psm != null) {
                psm.close();
                psm = null;
            }
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private interface OnResultSetHandler {
        void setPreparedStatement(PreparedStatement ps);
        Object handleResult(ResultSet rs);
    }
}

import javax.servlet.http.HttpServlet;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class CreateTables extends HttpServlet {

// Two tables: 'users' and 'photos'

    /*

    Table 'users'
    id = int AI PK
    username = varchar(45)
    password = varchar(45)

    Table 'photos'
    ID = int AI PK
    UserID = int
    filename = varchar(20)
    caption = varchar(20)
    datetaken = date

     */

}

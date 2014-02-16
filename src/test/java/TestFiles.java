import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Test;

import com.lightassistant.utility.FileUtil;


public class TestFiles
{
  @Test
  public void testDirCopy() throws IOException
  {
    FileUtil.copyDir("tests/fileIO/a/", "tests/FileIO/b/");
    FileUtil.mkDir("c", Paths.get("tests/fileIO/"));
    FileUtil.list("tests/fileIO/");
    FileUtil.rmDir("tests/fileIO/b/");
    FileUtil.rmDir("tests/fileIO/c/");
    FileUtil.list("tests/fileIO/");
  }
}

package com.acc.common.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

/**
 * 文件IO读写操作。
 * 作者：徐宏明 on 2018/6/4.
 * 邮箱：294985925@qq.com
 */
public class AccFileIOUtil {

    private static int sBufferSize = 8192;

    private AccFileIOUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 将输入流写文件路径
     *
     * @param filePath 待写入流的文件地址
     * @param is       待输入的流对象
     * @return {@code true}: 写入成功<br>{@code false}: 写入失败
     */
    public static boolean writeFileFromIS(final String filePath, final InputStream is) {
        return writeFileFromIS(getFileByPath(filePath), is, false);
    }

    /**
     * 将输入流追加到指定路径文件的后面。
     *
     * @param filePath 待写入流的文件地址
     * @param is       待输入的流对象
     * @param append   true追加，false不追加
     * @return {@code true}: 写入成功<br>{@code false}: 写入失败
     */
    public static boolean writeFileFromIS(final String filePath,
                                          final InputStream is,
                                          final boolean append) {
        return writeFileFromIS(getFileByPath(filePath), is, append);
    }

    /**
     * 将输入流写文件
     *
     * @param file 待写入流的文件地址
     * @param is   待输入的流对象
     * @return {@code true}: 写入成功<br>{@code false}: 写入失败
     */
    public static boolean writeFileFromIS(final File file, final InputStream is) {
        return writeFileFromIS(file, is, false);
    }

    /**
     * 将输入流追加到指定路径文件的后面。
     *
     * @param file   待写入流的文件地址
     * @param is     待输入的流对象
     * @param append true追加，false不追加
     * @return {@code true}: 写入成功<br>{@code false}: 写入失败
     */
    public static boolean writeFileFromIS(final File file,
                                          final InputStream is,
                                          final boolean append) {
        if (!createOrExistsFile(file) || is == null) return false;
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file, append));
            byte data[] = new byte[sBufferSize];
            int len;
            while ((len = is.read(data, 0, sBufferSize)) != -1) {
                os.write(data, 0, len);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将字节文件写入指定路径的文件中。
     *
     * @param filePath 文件的路径
     * @param bytes    字节对象
     * @return {@code true}: 写入成功<br>{@code false}: 写入失败
     */
    public static boolean writeFileFromBytesByStream(final String filePath, final byte[] bytes) {
        return writeFileFromBytesByStream(getFileByPath(filePath), bytes, false);
    }

    /**
     * 将字节文件写入指定路径的文件中。
     *
     * @param filePath 文件的路径
     * @param bytes    字节对象
     * @param append   true追加，false不追加
     * @return {@code true}: 写入成功<br>{@code false}: 写入失败
     */
    public static boolean writeFileFromBytesByStream(final String filePath,
                                                     final byte[] bytes,
                                                     final boolean append) {
        return writeFileFromBytesByStream(getFileByPath(filePath), bytes, append);
    }


    /**
     * 将字节文件写文件
     *
     * @param file  待写入流的文件地址
     * @param bytes 字节对象
     * @return {@code true}: 写入成功<br>{@code false}: 写入失败
     */
    public static boolean writeFileFromBytesByStream(final File file, final byte[] bytes) {
        return writeFileFromBytesByStream(file, bytes, false);
    }

    /**
     * 将字节文件写文件
     *
     * @param file   待写入流的文件地址
     * @param bytes  字节对象
     * @param append true追加，false不追加
     * @return {@code true}: 写入成功<br>{@code false}: 写入失败
     */
    public static boolean writeFileFromBytesByStream(final File file,
                                                     final byte[] bytes,
                                                     final boolean append) {
        if (bytes == null || !createOrExistsFile(file)) return false;
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file, append));
            bos.write(bytes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将字节文件通过管道的形式驶入指定路径的文件。
     *
     * @param filePath 文件的名字。
     * @param bytes    待写入的字节对象。
     * @param isForce  true强制写入，false不强制
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean writeFileFromBytesByChannel(final String filePath,
                                                      final byte[] bytes,
                                                      final boolean isForce) {
        return writeFileFromBytesByChannel(getFileByPath(filePath), bytes, false, isForce);
    }

    /**
     * 将字节文件通过管道的形式驶入指定路径的文件。
     *
     * @param filePath 文件的名字。
     * @param bytes    待写入的字节对象。
     * @param append   true追加，false不追加
     * @param isForce  true强制写入，false不强制
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean writeFileFromBytesByChannel(final String filePath,
                                                      final byte[] bytes,
                                                      final boolean append,
                                                      final boolean isForce) {
        return writeFileFromBytesByChannel(getFileByPath(filePath), bytes, append, isForce);
    }


    /**
     * 将字节文件通过管道的形式驶入指定文件。
     *
     * @param file    文件的名字。
     * @param bytes   待写入的字节对象。
     * @param isForce true强制写入，false不强制
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean writeFileFromBytesByChannel(final File file,
                                                      final byte[] bytes,
                                                      final boolean isForce) {
        return writeFileFromBytesByChannel(file, bytes, false, isForce);
    }

    /**
     * 将字节文件通过管道的形式驶入指定文件。
     *
     * @param file    文件的名字。
     * @param bytes   待写入的字节对象。
     * @param append  true追加，false不追加
     * @param isForce true强制写入，false不强制
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean writeFileFromBytesByChannel(final File file,
                                                      final byte[] bytes,
                                                      final boolean append,
                                                      final boolean isForce) {
        if (bytes == null) return false;
        FileChannel fc = null;
        try {
            fc = new FileOutputStream(file, append).getChannel();
            fc.position(fc.size());
            fc.write(ByteBuffer.wrap(bytes));
            if (isForce) fc.force(true);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fc != null) {
                    fc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 使用内存映射的方式存储文件。
     *
     * @param filePath 文件的路径。
     * @param bytes    待写入的字节对象。
     * @param isForce  true强制写入数据，false不强制写入。
     * @return {@code true}: 写入成功<br>{@code false}: 写入失败
     */
    public static boolean writeFileFromBytesByMap(final String filePath,
                                                  final byte[] bytes,
                                                  final boolean isForce) {
        return writeFileFromBytesByMap(filePath, bytes, false, isForce);
    }

    /**
     * 使用内存映射的方式存储文件。
     *
     * @param filePath 文件的路径。
     * @param bytes    待写入的字节对象。
     * @param append   true为追加数据，false不追加。
     * @param isForce  true强制写入数据，false不强制写入。
     * @return {@code true}: 写入成功<br>{@code false}: 写入失败
     */
    public static boolean writeFileFromBytesByMap(final String filePath,
                                                  final byte[] bytes,
                                                  final boolean append,
                                                  final boolean isForce) {
        return writeFileFromBytesByMap(getFileByPath(filePath), bytes, append, isForce);
    }

    /**
     * 使用内存映射的方式存储文件。
     *
     * @param file    文件的名字。
     * @param bytes   待写入的字节对象。
     * @param isForce true强制写入数据，false不强制写入。
     * @return {@code true}: 写入成功<br>{@code false}: 写入失败
     */
    public static boolean writeFileFromBytesByMap(final File file,
                                                  final byte[] bytes,
                                                  final boolean isForce) {
        return writeFileFromBytesByMap(file, bytes, false, isForce);
    }

    /**
     * 使用内存映射的方式存储文件。
     *
     * @param file    文件的名字。
     * @param bytes   待写入的字节对象。
     * @param append  true为追加数据，false不追加。
     * @param isForce true强制写入数据，false不强制写入。
     * @return {@code true}: 写入成功<br>{@code false}: 写入失败
     */
    public static boolean writeFileFromBytesByMap(final File file,
                                                  final byte[] bytes,
                                                  final boolean append,
                                                  final boolean isForce) {
        if (bytes == null || !createOrExistsFile(file)) return false;
        FileChannel fc = null;
        try {
            fc = new FileOutputStream(file, append).getChannel();
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, fc.size(), bytes.length);
            mbb.put(bytes);
            if (isForce) mbb.force();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fc != null) {
                    fc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将字符串写入文件中。
     *
     * @param filePath 文件路径。
     * @param content  字符串内容。
     * @return {@code true}: 写入成功<br>{@code false}: 写入失败
     */
    public static boolean writeFileFromString(final String filePath, final String content) {
        return writeFileFromString(getFileByPath(filePath), content, false);
    }

    /**
     * 将字符串写入文件中。
     *
     * @param filePath 文件路径。
     * @param content  字符串内容。
     * @param append   true为追加，false不追加。
     * @return {@code true}: 写入成功<br>{@code false}: 写入失败
     */
    public static boolean writeFileFromString(final String filePath,
                                              final String content,
                                              final boolean append) {
        return writeFileFromString(getFileByPath(filePath), content, append);
    }

    /**
     * 将字符串写入文件中。
     *
     * @param file    文件名字。
     * @param content 字符串内容。
     * @return {@code true}: 写入成功<br>{@code false}: 写入失败
     */
    public static boolean writeFileFromString(final File file, final String content) {
        return writeFileFromString(file, content, false);
    }

    /**
     * 将字符串写入文件中。
     *
     * @param file    文件名字。
     * @param content 字符串内容。
     * @param append  true为追加，false不追加。
     * @return {@code true}: 写入成功<br>{@code false}: 写入失败
     */
    public static boolean writeFileFromString(final File file,
                                              final String content,
                                              final boolean append) {
        if (file == null || content == null) return false;
        if (!createOrExistsFile(file)) return false;
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file, append));
            bw.write(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将下载的文件保存到指定的文件夹中，将进度发送给监听者。
     *
     * @param body    返回体
     * @param saveUrl 保存文件的路径
     * @return true为保存成功
     */
    public static boolean writeResponseBodyToDisk(ResponseBody body, String saveUrl, SingleLiveEvent<Integer> result) {
        try {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[1024];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(saveUrl);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    if (null != result)
                        result.postValue((int) (fileSizeDownloaded * 100 / fileSize));
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
    ///////////////////////////////////////////////////////////////////////////
    // the divide line of write and read
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 从指定文件按行读取字符串。
     *
     * @param filePath 待读取的文件路径。
     * @return 指定行的数据。
     */
    public static List<String> readFile2List(final String filePath) {
        return readFile2List(getFileByPath(filePath), null);
    }

    /**
     * 从指定文件按行读取字符串。
     *
     * @param filePath 待读取的文件路径。
     * @return 指定行的数据。
     */
    public static List<String> readFile2List(final String filePath, final String charsetName) {
        return readFile2List(getFileByPath(filePath), charsetName);
    }

    /**
     * 从指定文件按行读取字符串。
     *
     * @param file 待读取的文件名字。
     * @return 指定行的数据。
     */
    public static List<String> readFile2List(final File file) {
        return readFile2List(file, 0, 0x7FFFFFFF, null);
    }

    /**
     * 从指定文件按行读取字符串。
     *
     * @param file        待读取的文件名字。
     * @param charsetName 字符集。
     * @return 指定行的数据。
     */
    public static List<String> readFile2List(final File file, final String charsetName) {
        return readFile2List(file, 0, 0x7FFFFFFF, charsetName);
    }

    /**
     * 从指定文件中读取指定起始行的数据。
     *
     * @param filePath 待读取的文件路径。
     * @param st       起始行。
     * @param end      结束行。
     * @return 指定行的数据。
     */
    public static List<String> readFile2List(final String filePath, final int st, final int end) {
        return readFile2List(getFileByPath(filePath), st, end, null);
    }


    /**
     * 从指定文件中读取指定起始行的数据。
     *
     * @param filePath    待读取的文件路径。
     * @param st          起始行。
     * @param end         结束行。
     * @param charsetName 字符集。
     * @return 指定行的数据。
     */
    public static List<String> readFile2List(final String filePath,
                                             final int st,
                                             final int end,
                                             final String charsetName) {
        return readFile2List(getFileByPath(filePath), st, end, charsetName);
    }

    /**
     * 从指定文件中读取指定起始行的数据。
     *
     * @param file 待读取的文件名字。
     * @param st   起始行。
     * @param end  结束行。
     * @return 指定行的数据。
     */
    public static List<String> readFile2List(final File file, final int st, final int end) {
        return readFile2List(file, st, end, null);
    }

    /**
     * 从指定文件中读取指定起始行的数据。
     *
     * @param file        待读取的文件名字。
     * @param st          起始行。
     * @param end         结束行。
     * @param charsetName 字符集。
     * @return 指定行的数据。
     */
    public static List<String> readFile2List(final File file,
                                             final int st,
                                             final int end,
                                             final String charsetName) {
        if (!isFileExists(file)) return null;
        if (st > end) return null;
        BufferedReader reader = null;
        try {
            String line;
            int curLine = 1;
            List<String> list = new ArrayList<>();
            if (isSpace(charsetName)) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            } else {
                reader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(file), charsetName)
                );
            }
            while ((line = reader.readLine()) != null) {
                if (curLine > end) break;
                if (st <= curLine && curLine <= end) list.add(line);
                ++curLine;
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将指定路径的文件转换为字符串。
     *
     * @param filePath 文件的路径。
     * @return 字符串。
     */
    public static String readFile2String(final String filePath) {
        return readFile2String(getFileByPath(filePath), null);
    }

    /**
     * 将指定路径的文件转换为字符串。
     *
     * @param filePath    文件的路径。
     * @param charsetName 字符集。
     * @return 字符串。
     */
    public static String readFile2String(final String filePath, final String charsetName) {
        return readFile2String(getFileByPath(filePath), charsetName);
    }

    /**
     * 将指定路径的文件转换为字符串。
     *
     * @param file 文件的名字。
     * @return 字符串。
     */
    public static String readFile2String(final File file) {
        return readFile2String(file, null);
    }

    /**
     * 将指定路径的文件转换为字符串。
     *
     * @param file        文件的名字。
     * @param charsetName 字符集。
     * @return 字符串。
     */
    public static String readFile2String(final File file, final String charsetName) {
        byte[] bytes = readFile2BytesByStream(file);
        if (bytes == null) return null;
        if (isSpace(charsetName)) {
            return new String(bytes);
        } else {
            try {
                return new String(bytes, charsetName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
        }
    }

    /**
     * 将指定路径的文件转换为字节数组。
     *
     * @param filePath 文件路径。
     * @return 返回的字节数组。
     */
    public static byte[] readFile2BytesByStream(final String filePath) {
        return readFile2BytesByStream(getFileByPath(filePath));
    }

    /**
     * 将指定路径的文件转换为字节数组。
     *
     * @param file 文件名字。
     * @return 返回的字节数组。
     */
    public static byte[] readFile2BytesByStream(final File file) {
        if (!isFileExists(file)) return null;
        try {
            return is2Bytes(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将指定路径的文件通过管道形式转换为字节数组。
     *
     * @param filePath 文件路径。
     * @return 返回的字节数组。
     */
    public static byte[] readFile2BytesByChannel(final String filePath) {
        return readFile2BytesByChannel(getFileByPath(filePath));
    }

    /**
     * 将指定路径的文件通过管道形式转换为字节数组。
     *
     * @param file 文件路径。
     * @return 返回的字节数组。
     */
    public static byte[] readFile2BytesByChannel(final File file) {
        if (!isFileExists(file)) return null;
        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(file, "r").getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) fc.size());
            while (true) {
                if (!((fc.read(byteBuffer)) > 0)) break;
            }
            return byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fc != null) {
                    fc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将指定路径的文件通过管道形式转换为字节数组。
     *
     * @param filePath 文件路径。
     * @return 返回的字节数组。
     */
    public static byte[] readFile2BytesByMap(final String filePath) {
        return readFile2BytesByMap(getFileByPath(filePath));
    }

    /**
     * 将指定路径的文件通过管道形式转换为字节数组。
     *
     * @param file 文件路径。
     * @return 返回的字节数组。
     */
    public static byte[] readFile2BytesByMap(final File file) {
        if (!isFileExists(file)) return null;
        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(file, "r").getChannel();
            int size = (int) fc.size();
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, size).load();
            byte[] result = new byte[size];
            mbb.get(result, 0, size);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fc != null) {
                    fc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Set the buffer's size.
     * <p>Default size equals 8192 bytes.</p>
     *
     * @param bufferSize The buffer's size.
     */
    public static void setBufferSize(final int bufferSize) {
        sBufferSize = bufferSize;
    }

    private static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    private static boolean createOrExistsFile(final String filePath) {
        return createOrExistsFile(getFileByPath(filePath));
    }

    private static boolean createOrExistsFile(final File file) {
        if (file == null) return false;
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    private static boolean isFileExists(final File file) {
        return file != null && file.exists();
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static byte[] is2Bytes(final InputStream is) {
        if (is == null) return null;
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            byte[] b = new byte[sBufferSize];
            int len;
            while ((len = is.read(b, 0, sBufferSize)) != -1) {
                os.write(b, 0, len);
            }
            return os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

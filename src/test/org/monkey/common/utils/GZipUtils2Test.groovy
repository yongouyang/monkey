package org.monkey.common.utils

import org.junit.Test
import java.nio.charset.Charset


class GZipUtils2Test {

    def original = """
    { "success": "success" }
    """

    @Test
    public void compressAndThenDecompressReturnsTheOriginalInputWithDefaultUTF8Charset() {
        def compressed = GZipUtils2.compress(original)
        assert compressed == [31, -117, 8, 0, 0, 0, 0, 0, 0, 0, -29, 82, 0, -126, 106, 5, -91, -30, -46, -28, -28, -44, -30, 98, 37, 43, 4, 83, -95, -106, 11, 36, 9, 0, 75, 79, -46, -66, 34, 0, 0, 0]

        def decompressed = GZipUtils2.decompress(compressed)
        assert decompressed == original
    }

    @Test
    public void compressAndThenDecompressReturnsTheOriginalInputWithISO88591Charset() {
        def charset = Charset.forName("ISO-8859-1")
        def compressed = GZipUtils2.compress(original, charset)
        assert compressed == [31, -117, 8, 0, 0, 0, 0, 0, 0, 0, -29, 82, 0, -126, 106, 5, -91, -30, -46, -28, -28, -44, -30, 98, 37, 43, 4, 83, -95, -106, 11, 36, 9, 0, 75, 79, -46, -66, 34, 0, 0, 0]

        def decompressed = GZipUtils2.decompress(compressed, charset)
        assert decompressed == original
    }

}

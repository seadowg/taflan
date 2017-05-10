package com.seadowg.taflan.test.csv

import com.seadowg.taflan.csv.CSVParser
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.StringReader

class CSVParserTest {

    @Test
    fun whenFileIsCSV_returnsValid() {
        val reader = StringReader("field1,field2\nhello,hi\ngoodbye,see ya").buffered()

        val result = CSVParser().parse(reader)
        assertThat(result).isInstanceOf(CSVParser.Result.Valid::class.java)

        val csv = (result as CSVParser.Result.Valid).csv
        assertThat(csv.headers).isEqualTo(listOf("field1", "field2"))
        assertThat(csv.rows).isEqualTo(listOf(
                listOf("hello", "hi"),
                listOf("goodbye", "see ya")
        ))
    }

    @Test
    fun whenFileIsEmpty_returnsEmpty() {
        val reader = StringReader("").buffered()

        val csv = CSVParser().parse(reader)
        assertThat(csv).isInstanceOf(CSVParser.Result.Empty::class.java)
    }

    @Test
    fun whenFileIsGarbage_returnsInvalid() {
        val reader = StringReader("damn\n I suck,,,").buffered()

        val csv = CSVParser().parse(reader)
        assertThat(csv).isInstanceOf(CSVParser.Result.Invalid::class.java)
    }

    @Test
    fun whenFileContainsQuotedValuesWithCommas_returnsValid() {
        val reader = StringReader("field1\n\"hello,hi\"\n\"goodbye,see ya\"").buffered()

        val result = CSVParser().parse(reader)
        assertThat(result).isInstanceOf(CSVParser.Result.Valid::class.java)

        val csv = (result as CSVParser.Result.Valid).csv
        assertThat(csv.headers).isEqualTo(listOf("field1"))
        assertThat(csv.rows).isEqualTo(listOf(
                listOf("hello,hi"),
                listOf("goodbye,see ya")
        ))
    }

    @Test
    fun whenFileContainsQuotedValuesWithNewLines_returnsValid() {
        val reader = StringReader("field1\n\"hello \n hi\"\n\"goodbye \n see ya\"").buffered()

        val result = CSVParser().parse(reader)
        assertThat(result).isInstanceOf(CSVParser.Result.Valid::class.java)

        val csv = (result as CSVParser.Result.Valid).csv
        assertThat(csv.headers).isEqualTo(listOf("field1"))
        assertThat(csv.rows).isEqualTo(listOf(
                listOf("hello \n hi"),
                listOf("goodbye \n see ya")
        ))
    }
}

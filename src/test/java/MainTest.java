import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class MainTest {
    @Test
    public void testCase1() {
        String input = """
                4
                гвоздь
                шуруп
                краска синяя
                ведро для воды
                3
                краска
                корыто для воды
                шуруп 3х1.5
                """;
        String expected = """
                гвоздь:?\r
                шуруп:шуруп 3х1.5\r
                краска синяя:краска\r
                ведро для воды:корыто для воды\r
                """;
        Main.readInput(new ByteArrayInputStream(input.getBytes()));
        Main.process();
        OutputStream output = new ByteArrayOutputStream();
        Main.writeOutput(output);
        assertThat(output.toString(), is(expected));
    }

    @Test
    public void testCase2() {
        String input = """
                1
                Бетон с присадкой
                1
                Цемент
                """;
        String expected = """
                Бетон с присадкой:Цемент\r
                """;
        Main.readInput(new ByteArrayInputStream(input.getBytes()));
        Main.process();
        OutputStream output = new ByteArrayOutputStream();
        Main.writeOutput(output);
        assertThat(output.toString(), is(expected));
    }

    @Test
    public void testCase3() {
        String input = """
                1
                Бетон с присадкой
                2
                присадка для бетона
                доставка
                """;
        String expected = """
                Бетон с присадкой:присадка для бетона\r
                доставка:?\r
                """;
        Main.readInput(new ByteArrayInputStream(input.getBytes()));
        Main.process();
        OutputStream output = new ByteArrayOutputStream();
        Main.writeOutput(output);
        assertThat(output.toString(), is(expected));
    }
}
package com.github.ffpojo.decorator;

import com.github.ffpojo.FFPojoHelper;
import com.github.ffpojo.metadata.positional.annotation.EnumerationType;
import com.github.ffpojo.metadata.positional.annotation.PositionalField;
import com.github.ffpojo.metadata.positional.annotation.PositionalRecord;
import com.github.ffpojo.metadata.positional.annotation.extra.DatePositionalField;
import com.github.ffpojo.metadata.positional.annotation.extra.EnumPositionalField;
import com.github.ffpojo.metadata.positional.annotation.extra.PositionalFieldRemainder;
import com.google.common.truth.Truth;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by William on 02/11/15.
 */
public class EnumDecoratorTest {

    @Test
    public void shoudToTestEnumDecoratorForStringType() throws ParseException {
        final FFpojoGitLog fFpojoGitLog = new FFpojoGitLog();
        fFpojoGitLog.setColaborator(Colaborator.WILLIAM);
        fFpojoGitLog.setDateCommit(new SimpleDateFormat("ddMMyyyy").parse("02112015"));
        fFpojoGitLog.setNome("Manoel");
        fFpojoGitLog.setMessage("Teste anotacao @EnumPositionalField");
        String expected = FFPojoHelper.getInstance().parseToText(fFpojoGitLog);
        System.out.println(expected);
        FFpojoGitLog other =  FFPojoHelper.getInstance().createFromText(FFpojoGitLog.class, expected);
        String actual = FFPojoHelper.getInstance().parseToText(other);
        System.out.println(actual);
        Truth.assert_().that(actual).isEqualTo(expected);
        Truth.assert_().that(other).isEqualTo(fFpojoGitLog);
        Truth.assert_().that(other.getColaborator()).isEqualTo(Colaborator.WILLIAM);

    }
    @Test
    public void shoudToTestEnumDecoratorForOrdinalType(){
        final FFpojoGitLogOrdinal fFpojoGitLog = new FFpojoGitLogOrdinal();
        fFpojoGitLog.setColaborator(Colaborator.BARBERO);
        fFpojoGitLog.setDateCommit(new Date());
        fFpojoGitLog.setMessage("Teste anotacao @EnumPositionalField");
        fFpojoGitLog.setNome("Josefina");
        String expected = FFPojoHelper.getInstance().parseToText(fFpojoGitLog);
        System.out.println(expected);
        FFpojoGitLog other =  FFPojoHelper.getInstance().createFromText(FFpojoGitLogOrdinal.class, expected);
        String actual = FFPojoHelper.getInstance().parseToText(other);
        Truth.assert_().that(actual).isEqualTo(expected);
        Truth.assert_().that(other).isEqualTo(fFpojoGitLog);
        Truth.assert_().that(other.getColaborator()).isEqualTo(Colaborator.BARBERO);

    }

    enum Colaborator{
        WILLIAM, GILBERTO, BARBERO, DVBEATO, NORONHA
    }
    @PositionalRecord
    public static class FFpojoGitLogOrdinal extends FFpojoGitLog {

        @EnumPositionalField(initialPosition = 9, finalPosition = 20, enumClass = Colaborator.class, enumType = EnumerationType.ORDINAL)
        private Colaborator colaborator;


        @Override
        public Colaborator getColaborator() {
            return colaborator;
        }

        @Override
        public void setColaborator(Colaborator colaborator) {
            this.colaborator = colaborator;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FFpojoGitLogOrdinal that = (FFpojoGitLogOrdinal) o;

            return colaborator == that.colaborator;

        }

        @Override
        public int hashCode() {
            return colaborator.hashCode();
        }
    }

    @PositionalRecord
    public static class FFpojoGitLog{

        @DatePositionalField(initialPosition = 1, finalPosition = 8, dateFormat = "ddMMyyyy")
        private Date dateCommit;

        @EnumPositionalField(initialPosition = 9 ,  finalPosition = 20, enumClass = Colaborator.class)
        private Colaborator colaborator;

        @PositionalField(initialPosition = 21, finalPosition = 30)
        private String nome;

        @PositionalFieldRemainder
        private String message;

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public Date getDateCommit() {
            return dateCommit;
        }

        public void setDateCommit(Date dateCommit) {
            this.dateCommit = dateCommit;
        }

        public Colaborator getColaborator() {
            return colaborator;
        }

        public void setColaborator(Colaborator colaborator) {
            this.colaborator = colaborator;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FFpojoGitLog that = (FFpojoGitLog) o;

            if (!dateCommit.equals(that.dateCommit)) return false;
            if (colaborator != that.colaborator) return false;
            if (!nome.equals(that.nome)) return false;
            return message.equals(that.message);

        }

        @Override
        public int hashCode() {
            int result = dateCommit != null ? dateCommit.hashCode() : 0;
            result = 31 * result + (colaborator != null ? colaborator.hashCode() : 0);
            result = 31 * result + (nome != null ? nome.hashCode() : 0);
            result = 31 * result + (message != null ? message.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "FFpojoGitLog{" +
                    "dateCommit=" + dateCommit +
                    ", colaborator=" + colaborator +
                    ", nome='" + nome + '\'' +
                    ", message='" + message + '\'' +
                    '}';
        }
    }


}

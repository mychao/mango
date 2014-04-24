/*
 * Copyright 2014 mango.concurrent.cc
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package cc.concurrent.mango.exception;

import cc.concurrent.mango.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * 测试{@link GeneratedKeysException}
 *
 * @author ash
 */
public class GeneratedKeysExceptionTest {


    private final static DataSource ds = Config.getDataSource();
    private final static Mango mango = new Mango(ds);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void before() throws Exception {
        Connection conn = ds.getConnection();
        Sqls.PERSON.run(conn);
        conn.close();
    }

    @Test
    public void test() {
        thrown.expect(GeneratedKeysException.class);
        thrown.expectMessage("please check whether the table has auto increment key");
        PsersonDao dao = mango.create(PsersonDao.class);
        Person p = new Person(1, "ash");
        dao.add(p);
    }

    @DB
    static interface PsersonDao {

        @ReturnGeneratedId
        @SQL("insert into person(id, name) values(:1.id, :1.name)")
        public int add(Person p);
    }


    public static class Person {
        int id;
        String name;

        public Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

    }

}








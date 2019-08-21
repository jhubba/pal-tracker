package io.pivotal.pal.tracker;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into time_entries ( project_id, user_id, date, hours) " +
                            "values( ?, ?, ?, ?)",
                    RETURN_GENERATED_KEYS
            );

            preparedStatement.setLong(1, timeEntry.getProjectId());
            preparedStatement.setLong(2, timeEntry.getUserId());
            preparedStatement.setDate(3, Date.valueOf(timeEntry.getDate()));
            preparedStatement.setInt(4, timeEntry.getHours());

            return preparedStatement;

        }, generatedKeyHolder);

        return find(generatedKeyHolder.getKey().longValue());


    }

    @Override
    public TimeEntry find(Long timeEntryId) {
        return jdbcTemplate.query(
                "select id, project_id, user_id, date, hours from time_entries where id = ? ",
                new Object[]{timeEntryId},
                extractor);
    }

    @Override
    public TimeEntry update(Long timeEntryId, TimeEntry timeEntry) {
         jdbcTemplate.update("update time_entries " +
                "set project_id = ? , user_id = ?, date = ?, hours = ? where id = ? ",
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                Date.valueOf(timeEntry.getDate()),
                timeEntry.getHours(),
                timeEntryId);

        return find(timeEntryId);

    }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query(
                "select id, project_id, user_id, date, hours from time_entries",
                mapper);
    }

    @Override
    public void delete(Long timeEntryId) {
        jdbcTemplate.update("delete from time_entries where id = ?", timeEntryId);
    }

    private final RowMapper<TimeEntry> mapper = (rs, rowNum) -> new TimeEntry(
            rs.getLong("id"),
            rs.getLong("project_id"),
            rs.getLong("user_id"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("hours")
    );

    private final ResultSetExtractor<TimeEntry> extractor =
            (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;
}

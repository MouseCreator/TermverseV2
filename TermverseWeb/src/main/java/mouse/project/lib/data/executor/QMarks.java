package mouse.project.lib.data.executor;

import mouse.project.lib.data.exception.ExecutorException;

import java.util.List;

public class QMarks {
    public static void validateQMarks(String sql, List<?> args) {
        int questionMarks = getQuestionMarks(sql);
        if (args.size() != questionMarks) {
            String msg = String.format("Input sql has %d place holders, but received %d arguments", questionMarks, args.size());
            throw new ExecutorException(msg);
        }
    }

    private static int getQuestionMarks(String sql) {
        long count = sql.chars().filter(ch -> ch == '?').count();
        return (int) count;
    }
}

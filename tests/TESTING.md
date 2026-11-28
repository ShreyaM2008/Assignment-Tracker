
# Assignment Tracker Testing

## Testing Method

Manual testing was performed on the Java Swing application
to verify its core functionality, input validation, sorting,
and persistent data storage.

## Test Results

| Test | Expected Result | Result |
|---|---|---|
| Add valid assignments | Assignments are added successfully | PASS |
| View assignments | Assignments appear in the table | PASS |
| Submit empty fields | Error message appears | PASS |
| Enter an invalid date | Error message appears | PASS |
| Enter a comma in the title | Error message appears | PASS |
| Complete without selecting | Error message appears | PASS |
| Mark an assignment completed | Status changes to Completed | PASS |
| Verify assignment counters | Counters update correctly | PASS |
| Complete an already completed assignment | Warning message appears | PASS |
| Sort by due date | Assignments appear in chronological order | PASS |
| Click Save & Exit | Data is saved and the window closes | PASS |
| Reopen the application | Saved assignments and statuses are restored | PASS |

## Testing Summary

- Total tests: 12
- Passed: 12
- Failed: 0

## Conclusion

All tested MVP features and the additional Schedule feature
worked as expected. Input validation successfully handled
the tested invalid inputs, and CSV storage preserved
assignment information between sessions.

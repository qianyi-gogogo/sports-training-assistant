# Database
MySQL 8.0 baseline exported on 2026-11-24. Run schema files 00 through 07 in numeric order on a new database. CREATE TABLE IF NOT EXISTS does not upgrade existing tables. Do not execute the original Workbench dump on a populated database: it contains DROP TABLE statements. No user data included. Existing constraints and column types are preserved. Future schema changes belong in migrations; synthetic fixtures in test; inspection SQL in queries.

## Password reset codes
For an existing database, run `schema/07_password_reset_codes.sql` once if the new table is missing. The legacy `password_reset_tokens` table is no longer used by the code-based recovery flow. Keep it until local testing is complete; then it may be removed separately after confirming there is no legacy application instance using it.

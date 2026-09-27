-- Migration 001: Add exam_results and timetable tables
-- Run this against your Supabase project via the SQL editor or CLI.
-- Project: dibxccfjbntfnzhqpcuv (Tasaaga OVC Primary School)

-- =============================================================================
-- 23. EXAM RESULTS TABLE
-- =============================================================================
CREATE TABLE IF NOT EXISTS public.exam_results (
    id SERIAL PRIMARY KEY,
    student_id INTEGER NOT NULL REFERENCES public.students(id) ON DELETE CASCADE,
    student_name VARCHAR(255),
    adm_no VARCHAR(50),
    class_id INTEGER REFERENCES public.classes(id) ON DELETE SET NULL,
    subject VARCHAR(100) NOT NULL,
    marks INTEGER NOT NULL CHECK (marks >= 0 AND marks <= 100),
    max_marks INTEGER NOT NULL DEFAULT 100,
    grade VARCHAR(5),
    term INTEGER NOT NULL DEFAULT 1 CHECK (term IN (1, 2, 3)),
    academic_year INTEGER NOT NULL DEFAULT 2026,
    entered_by VARCHAR(255),
    school_id VARCHAR(100) REFERENCES public.school_info(id) ON DELETE CASCADE
);

-- Computed grade trigger: sets A/B/C/D/F automatically if grade not provided
CREATE OR REPLACE FUNCTION public.set_exam_grade()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.grade IS NULL THEN
        NEW.grade := CASE
            WHEN NEW.marks >= 80 THEN 'A'
            WHEN NEW.marks >= 65 THEN 'B'
            WHEN NEW.marks >= 50 THEN 'C'
            WHEN NEW.marks >= 40 THEN 'D'
            ELSE 'F'
        END;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_set_exam_grade ON public.exam_results;
CREATE TRIGGER trg_set_exam_grade
    BEFORE INSERT OR UPDATE ON public.exam_results
    FOR EACH ROW EXECUTE FUNCTION public.set_exam_grade();

-- Indexes for common query patterns
CREATE INDEX IF NOT EXISTS idx_exam_results_student  ON public.exam_results(student_id);
CREATE INDEX IF NOT EXISTS idx_exam_results_class    ON public.exam_results(class_id);
CREATE INDEX IF NOT EXISTS idx_exam_results_school   ON public.exam_results(school_id);
CREATE INDEX IF NOT EXISTS idx_exam_results_term_year ON public.exam_results(term, academic_year);

-- RLS
ALTER TABLE public.exam_results ENABLE ROW LEVEL SECURITY;
DROP POLICY IF EXISTS "Auth Read Exam Results" ON public.exam_results;
CREATE POLICY "Auth Read Exam Results"
    ON public.exam_results FOR SELECT TO authenticated USING (true);
DROP POLICY IF EXISTS "Auth Insert Exam Results" ON public.exam_results;
CREATE POLICY "Auth Insert Exam Results"
    ON public.exam_results FOR INSERT TO authenticated WITH CHECK (true);

-- =============================================================================
-- 24. TIMETABLE TABLE
-- =============================================================================
CREATE TABLE IF NOT EXISTS public.timetable (
    id SERIAL PRIMARY KEY,
    class_id INTEGER NOT NULL REFERENCES public.classes(id) ON DELETE CASCADE,
    day VARCHAR(10) NOT NULL CHECK (day IN ('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday')),
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    subject VARCHAR(100) NOT NULL,
    teacher VARCHAR(255),
    school_id VARCHAR(100) REFERENCES public.school_info(id) ON DELETE CASCADE,
    CONSTRAINT timetable_valid_time CHECK (end_time > start_time)
);

-- Index for fetching a class's weekly timetable
CREATE INDEX IF NOT EXISTS idx_timetable_class  ON public.timetable(class_id);
CREATE INDEX IF NOT EXISTS idx_timetable_school ON public.timetable(school_id);

-- RLS
ALTER TABLE public.timetable ENABLE ROW LEVEL SECURITY;
DROP POLICY IF EXISTS "Auth Read Timetable" ON public.timetable;
CREATE POLICY "Auth Read Timetable"
    ON public.timetable FOR SELECT TO authenticated USING (true);
DROP POLICY IF EXISTS "Auth Insert Timetable" ON public.timetable;
CREATE POLICY "Auth Insert Timetable"
    ON public.timetable FOR INSERT TO authenticated WITH CHECK (true);

-- =============================================================================
-- SEED DATA (optional — 7 sample exam results for testing)
-- Assumes school_id = 'tasaaga-school-id', class_id = 1, student_id = 1
-- =============================================================================
-- INSERT INTO public.exam_results
--     (student_id, student_name, adm_no, class_id, subject, marks, term, academic_year, entered_by, school_id)
-- VALUES
--     (1, 'Mary Achola', 'TAS-2026-001', 1, 'Mathematics',      78, 1, 2026, 'Ms. Sarah Amoko', 'tasaaga-school-id'),
--     (1, 'Mary Achola', 'TAS-2026-001', 1, 'English',          82, 1, 2026, 'Ms. Sarah Amoko', 'tasaaga-school-id'),
--     (1, 'Mary Achola', 'TAS-2026-001', 1, 'Science',          74, 1, 2026, 'Ms. Sarah Amoko', 'tasaaga-school-id'),
--     (1, 'Mary Achola', 'TAS-2026-001', 1, 'Social Studies',   69, 1, 2026, 'Ms. Sarah Amoko', 'tasaaga-school-id'),
--     (1, 'Mary Achola', 'TAS-2026-001', 1, 'Religious Education', 88, 1, 2026, 'Ms. Sarah Amoko', 'tasaaga-school-id'),
--     (1, 'Mary Achola', 'TAS-2026-001', 1, 'Physical Education', 91, 1, 2026, 'Ms. Sarah Amoko', 'tasaaga-school-id'),
--     (1, 'Mary Achola', 'TAS-2026-001', 1, 'Luganda',          65, 1, 2026, 'Ms. Sarah Amoko', 'tasaaga-school-id');

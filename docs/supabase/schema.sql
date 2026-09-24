-- =============================================================================
-- TASAAGA SCHOOL MANAGEMENT SYSTEM (TSMS) - SUPABASE POSTGRESQL SCHEMA
-- Official Database DDL Schema (SRS v1.1 Baseline)
-- Organization: Tasaaga Primary School, Sitabaale, Uganda
-- Motto: "Rising To Succeed"
-- =============================================================================

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 1. PROFILES TABLE (Mapped to Supabase Auth)
CREATE TABLE IF NOT EXISTS public.profiles (
    id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
    role VARCHAR(50) NOT NULL CHECK (role IN ('Admin', 'Headteacher', 'Teacher', 'Finance', 'Parent', 'Boarding', 'Student', 'Public')),
    email VARCHAR(255),
    name VARCHAR(255),
    phone VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- 2. SCHOOL INFO TABLE
CREATE TABLE IF NOT EXISTS public.school_info (
    id INT PRIMARY KEY DEFAULT 1,
    mission TEXT NOT NULL,
    motto VARCHAR(255) NOT NULL,
    programs TEXT[] DEFAULT '{}'
);

-- 3. DONATION INFO TABLE
CREATE TABLE IF NOT EXISTS public.donation_info (
    id INT PRIMARY KEY DEFAULT 1,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    donation_methods TEXT[] DEFAULT '{}',
    sponsorship_details TEXT NOT NULL
);

-- 4. NEWS ITEMS TABLE
CREATE TABLE IF NOT EXISTS public.news_items (
    id VARCHAR(50) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    category VARCHAR(50) NOT NULL CHECK (category IN ('CLINIC', 'COMMUNITY', 'SUCCESS_STORY')),
    date VARCHAR(50) NOT NULL,
    image_url TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- 5. VOLUNTEER OPPORTUNITIES TABLE
CREATE TABLE IF NOT EXISTS public.volunteer_opportunities (
    id VARCHAR(50) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    requirements TEXT[] DEFAULT '{}',
    duration VARCHAR(100) NOT NULL,
    accommodation TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- 6. ACADEMIC YEARS TABLE
CREATE TABLE IF NOT EXISTS public.academic_years (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'completed', 'upcoming'))
);

-- 7. TERMS TABLE
CREATE TABLE IF NOT EXISTS public.terms (
    id SERIAL PRIMARY KEY,
    academic_year_id INT REFERENCES public.academic_years(id) ON DELETE CASCADE,
    name VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'completed', 'upcoming'))
);

-- 8. CLASSES TABLE
CREATE TABLE IF NOT EXISTS public.classes (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    level INT NOT NULL,
    stream VARCHAR(10) NOT NULL DEFAULT 'A',
    teacher_name VARCHAR(255)
);

-- 9. SUBJECTS TABLE
CREATE TABLE IF NOT EXISTS public.subjects (
    id SERIAL PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    level VARCHAR(50) NOT NULL DEFAULT 'All',
    is_core BOOLEAN NOT NULL DEFAULT true
);

-- 10. PARENTS / GUARDIANS TABLE
CREATE TABLE IF NOT EXISTS public.parents (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    email VARCHAR(255),
    children TEXT[] DEFAULT '{}',
    status VARCHAR(20) NOT NULL DEFAULT 'Active'
);

-- 11. STUDENTS TABLE
CREATE TABLE IF NOT EXISTS public.students (
    id SERIAL PRIMARY KEY,
    adm_no VARCHAR(50) UNIQUE,
    fname VARCHAR(100),
    lname VARCHAR(100),
    dob DATE,
    gender VARCHAR(20) CHECK (gender IN ('Male', 'Female', 'Boy', 'Girl')),
    student_type VARCHAR(20) CHECK (student_type IN ('Day', 'Boarding')),
    class_id INT REFERENCES public.classes(id),
    guardian_id INT REFERENCES public.parents(id),
    guardian_name VARCHAR(255),
    guardian_phone VARCHAR(50),
    status VARCHAR(20) NOT NULL DEFAULT 'Active',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- 12. ATTENDANCE TABLE
CREATE TABLE IF NOT EXISTS public.attendance (
    id SERIAL PRIMARY KEY,
    student_id INT REFERENCES public.students(id) ON DELETE CASCADE,
    student_name VARCHAR(255),
    adm_no VARCHAR(50),
    class_id INT REFERENCES public.classes(id),
    status VARCHAR(20) CHECK (status IN ('Present', 'Absent', 'Late', 'Excused')),
    recorded_time VARCHAR(20),
    note TEXT,
    recorded_by VARCHAR(255),
    date DATE NOT NULL DEFAULT CURRENT_DATE
);

-- 13. FEE STRUCTURES TABLE
CREATE TABLE IF NOT EXISTS public.fee_structures (
    id SERIAL PRIMARY KEY,
    category VARCHAR(100) NOT NULL,
    student_type VARCHAR(20) NOT NULL CHECK (student_type IN ('Day', 'Boarding')),
    amount NUMERIC(12,2) NOT NULL,
    term VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Active'
);

-- 14. PAYMENTS TABLE
CREATE TABLE IF NOT EXISTS public.payments (
    id SERIAL PRIMARY KEY,
    receipt_no VARCHAR(50) UNIQUE,
    student_id INT REFERENCES public.students(id),
    student_name VARCHAR(255),
    class_id INT REFERENCES public.classes(id),
    amount NUMERIC(12,2),
    payment_method VARCHAR(50) CHECK (payment_method IN ('Cash', 'MTN Mobile Money', 'Airtel Money', 'Bank Transfer')),
    payment_date DATE NOT NULL DEFAULT CURRENT_DATE,
    recorded_by VARCHAR(255),
    status VARCHAR(20) NOT NULL DEFAULT 'Confirmed' CHECK (status IN ('Confirmed', 'Pending', 'Reversed')),
    reference_no VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- 15. EXPENSES TABLE
CREATE TABLE IF NOT EXISTS public.expenses (
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL DEFAULT CURRENT_DATE,
    category VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    requested_by VARCHAR(255) NOT NULL,
    approved_by VARCHAR(255),
    status VARCHAR(20) NOT NULL DEFAULT 'Pending' CHECK (status IN ('Approved', 'Pending', 'Rejected'))
);

-- 16. BOARDING DORMS TABLE
CREATE TABLE IF NOT EXISTS public.boarding_dorms (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    gender VARCHAR(20) NOT NULL CHECK (gender IN ('Boys', 'Girls')),
    capacity INT NOT NULL DEFAULT 30,
    occupied INT NOT NULL DEFAULT 0
);

-- 17. BOARDING ALLOCATIONS TABLE
CREATE TABLE IF NOT EXISTS public.boarding_allocations (
    id SERIAL PRIMARY KEY,
    student_id INT REFERENCES public.students(id) ON DELETE CASCADE,
    student_name VARCHAR(255) NOT NULL,
    class_id INT REFERENCES public.classes(id),
    dorm_name VARCHAR(100) NOT NULL,
    room VARCHAR(50) NOT NULL,
    bed VARCHAR(50) NOT NULL,
    allocated_from DATE NOT NULL DEFAULT CURRENT_DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'Active'
);

-- 18. BOARDING WELFARE TABLE
CREATE TABLE IF NOT EXISTS public.boarding_welfare (
    id SERIAL PRIMARY KEY,
    student_name VARCHAR(255) NOT NULL,
    incident_type VARCHAR(50) NOT NULL CHECK (incident_type IN ('Health', 'Discipline', 'General')),
    description TEXT NOT NULL,
    date DATE NOT NULL DEFAULT CURRENT_DATE,
    reported_by VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Active' CHECK (status IN ('Active', 'Resolved'))
);

-- 19. STAFF TABLE
CREATE TABLE IF NOT EXISTS public.staff (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(100) NOT NULL,
    department VARCHAR(100) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    email VARCHAR(255),
    joined_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Active'
);

-- 20. INVENTORY ASSETS & STOCK TABLES
CREATE TABLE IF NOT EXISTS public.inventory_assets (
    id SERIAL PRIMARY KEY,
    asset_code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    condition VARCHAR(50) NOT NULL CHECK (condition IN ('Good', 'Fair', 'Poor', 'Damaged')),
    location VARCHAR(255) NOT NULL,
    custodian VARCHAR(255) NOT NULL,
    estimated_value NUMERIC(12,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.inventory_stock (
    id SERIAL PRIMARY KEY,
    item_name VARCHAR(255) NOT NULL UNIQUE,
    category VARCHAR(100) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    min_level INT NOT NULL DEFAULT 10,
    unit VARCHAR(50) NOT NULL,
    low_stock_alert BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE IF NOT EXISTS public.inventory_movements (
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL DEFAULT CURRENT_DATE,
    item_name VARCHAR(255) NOT NULL,
    movement_type VARCHAR(20) NOT NULL CHECK (movement_type IN ('Issue', 'Receipt')),
    quantity INT NOT NULL,
    reason TEXT NOT NULL,
    recorded_by VARCHAR(255) NOT NULL
);

-- 21. ANNOUNCEMENTS TABLE
CREATE TABLE IF NOT EXISTS public.announcements (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    audience VARCHAR(50) NOT NULL CHECK (audience IN ('All', 'Parents', 'Students', 'Staff')),
    published_by VARCHAR(255) NOT NULL,
    date DATE NOT NULL DEFAULT CURRENT_DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'Published' CHECK (status IN ('Published', 'Draft'))
);

-- 22. AUDIT LOGS TABLE
CREATE TABLE IF NOT EXISTS public.audit_logs (
    id SERIAL PRIMARY KEY,
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    actor VARCHAR(255) NOT NULL,
    action VARCHAR(100) NOT NULL,
    entity VARCHAR(255) NOT NULL,
    details TEXT NOT NULL,
    ip_address VARCHAR(50)
);

-- =============================================================================
-- ROW LEVEL SECURITY (RLS) POLICIES
-- =============================================================================

ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.news_items ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.volunteer_opportunities ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.school_info ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.donation_info ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.students ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.payments ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Public Read News" ON public.news_items;
DROP POLICY IF EXISTS "Public Read Volunteer" ON public.volunteer_opportunities;
DROP POLICY IF EXISTS "Public Read School Info" ON public.school_info;
DROP POLICY IF EXISTS "Public Read Donation Info" ON public.donation_info;
DROP POLICY IF EXISTS "Auth Read Profiles" ON public.profiles;
DROP POLICY IF EXISTS "Auth Read Students" ON public.students;
DROP POLICY IF EXISTS "Auth Read Payments" ON public.payments;

CREATE POLICY "Public Read News" ON public.news_items FOR SELECT USING (true);
CREATE POLICY "Public Read Volunteer" ON public.volunteer_opportunities FOR SELECT USING (true);
CREATE POLICY "Public Read School Info" ON public.school_info FOR SELECT USING (true);
CREATE POLICY "Public Read Donation Info" ON public.donation_info FOR SELECT USING (true);
CREATE POLICY "Auth Read Profiles" ON public.profiles FOR SELECT TO authenticated USING (true);
CREATE POLICY "Auth Read Students" ON public.students FOR SELECT TO authenticated USING (true);
CREATE POLICY "Auth Read Payments" ON public.payments FOR SELECT TO authenticated USING (true);

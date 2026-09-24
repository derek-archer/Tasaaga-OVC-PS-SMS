-- =============================================================================
-- TASAAGA SCHOOL MANAGEMENT SYSTEM (TSMS) - SUPABASE SEED DATASET
-- Sample Seed Data for Tasaaga Primary School (Sitabaale, Uganda)
-- =============================================================================

-- 0. PROFILES / DEMO USERS (7 Roles)
INSERT INTO public.profiles (id, role, email, name, phone) VALUES
('3f32eff2-bae4-4ef5-a38f-a440ec984e16', 'Admin', 'admin@tasaagaschool.org', 'Admin User', '+256701000000'),
('a1b2c3d4-0001-4ef5-a38f-a440ec984e16', 'Headteacher', 'headteacher@tasaagaschool.org', 'Mrs. Rose Nakato', '+256701000001'),
('a1b2c3d4-0002-4ef5-a38f-a440ec984e16', 'Teacher', 'teacher@tasaagaschool.org', 'Ms. Sarah Amoko', '+256701000002'),
('a1b2c3d4-0003-4ef5-a38f-a440ec984e16', 'Finance', 'finance@tasaagaschool.org', 'Mr. David Okot', '+256701000004'),
('a1b2c3d4-0004-4ef5-a38f-a440ec984e16', 'Parent', 'parent@tasaagaschool.org', 'Mr. George Achola', '+256701234567'),
('a1b2c3d4-0005-4ef5-a38f-a440ec984e16', 'Boarding', 'boarding@tasaagaschool.org', 'Ms. Ruth Akello', '+256701000008'),
('a1b2c3d4-0006-4ef5-a38f-a440ec984e16', 'Student', 'student@tasaagaschool.org', 'Mary Achola', '+256701234567')
ON CONFLICT (id) DO UPDATE SET role = EXCLUDED.role, email = EXCLUDED.email, name = EXCLUDED.name;

-- 1. SCHOOL INFO
INSERT INTO public.school_info (id, mission, motto, programs) VALUES
(1, 'Rising To Succeed, self-reliance and holistic care for orphaned and vulnerable children.', 'Rising To Succeed', ARRAY['Primary School (Day & Boarding)', 'Secondary School', 'Vocational Training'])
ON CONFLICT (id) DO UPDATE SET mission = EXCLUDED.mission, motto = EXCLUDED.motto;

-- 2. DONATION INFO
INSERT INTO public.donation_info (id, title, description, donation_methods, sponsorship_details) VALUES
(1, 'Support Tasaaga OVC P/S', 'Your support provides quality education, boarding care, healthcare, and nutrition to orphaned and vulnerable children.', ARRAY['MTN Mobile Money', 'Airtel Money', 'Bank Transfer', 'Online Credit Card'], 'Sponsorship for OVC includes subsidized tuition fees, essential scholastic materials, meals, and boarding accommodation.')
ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title, description = EXCLUDED.description;

-- 3. NEWS ITEMS
INSERT INTO public.news_items (id, title, content, category, date, image_url) VALUES
('1', 'Musawo Clinic Update', 'The Musawo clinic has successfully treated over 100 community members and students this month with essential healthcare.', 'CLINIC', '2026-09-15', 'https://images.unsplash.com/photo-1584515979956-d9f6e5d09982?auto=format&fit=crop&q=80&w=600'),
('2', 'Success Story: Sarah''s Journey', 'Sarah, a former OVC student, has graduated from vocational training and started her own tailoring business in Sitabaale.', 'SUCCESS_STORY', '2026-09-10', 'https://images.unsplash.com/photo-1544717305-2782549b5136?auto=format&fit=crop&q=80&w=600'),
('3', 'Community Outreach Program', 'Our team visited local villages in Sitabaale to provide health education, clean water sensitization, and school support.', 'COMMUNITY', '2026-09-05', 'https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?auto=format&fit=crop&q=80&w=600')
ON CONFLICT (id) DO NOTHING;

-- 4. VOLUNTEER OPPORTUNITIES
INSERT INTO public.volunteer_opportunities (id, title, description, requirements, duration, accommodation) VALUES
('1', 'Mentorship Program', 'Mentor students in various academic subjects, computer literacy, and life skills.', ARRAY['3-6 month commitment', 'Passion for education', 'Background check'], '3-6 months', 'On-site accommodation provided'),
('2', 'Vocational Trainer', 'Teach vocational skills like tailoring, carpentry, agriculture, or IT.', ARRAY['Expertise in a craft/trade', 'Ability to teach', '2 months minimum'], '2+ months', 'On-site accommodation provided')
ON CONFLICT (id) DO NOTHING;

-- 5. ACADEMIC YEARS & TERMS
INSERT INTO public.academic_years (id, name, start_date, end_date, status) VALUES
(1, '2026', '2026-01-15', '2026-11-30', 'active')
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.terms (id, academic_year_id, name, start_date, end_date, status) VALUES
(1, 1, 'Term 1', '2026-01-15', '2026-04-10', 'completed'),
(2, 1, 'Term 2', '2026-05-05', '2026-07-31', 'completed'),
(3, 1, 'Term 3', '2026-08-25', '2026-11-15', 'active')
ON CONFLICT (id) DO NOTHING;

-- 6. CLASSES
INSERT INTO public.classes (id, name, level, stream, teacher_name) VALUES
(1, 'P.1A', 1, 'A', 'Mrs. Brenda Atim'),
(2, 'P.2A', 2, 'A', 'Mr. Felix Ojara'),
(3, 'P.3A', 3, 'A', 'Mrs. Grace Akello'),
(4, 'P.4A', 4, 'A', 'Mr. Charles Onen'),
(5, 'P.5A', 5, 'A', 'Ms. Sarah Amoko'),
(6, 'P.6A', 6, 'A', 'Mr. James Opio'),
(7, 'P.7A', 7, 'A', 'Mrs. Rose Nakayima')
ON CONFLICT (id) DO NOTHING;

-- 7. SUBJECTS
INSERT INTO public.subjects (id, code, name, level, is_core) VALUES
(1, 'MTH', 'Mathematics', 'All', true),
(2, 'ENG', 'English Language', 'All', true),
(3, 'SCI', 'Science', 'P.3-P.7', true),
(4, 'SST', 'Social Studies', 'P.3-P.7', true),
(5, 'RE', 'Religious Education', 'All', false)
ON CONFLICT (id) DO NOTHING;

-- 8. PARENTS
INSERT INTO public.parents (id, name, phone, email, children, status) VALUES
(1, 'Mr. George Achola', '+256701234567', 'george.achola@gmail.com', ARRAY['Mary Achola'], 'Active'),
(2, 'Mrs. Susan Okello', '+256702345678', NULL, ARRAY['James Okello'], 'Active'),
(3, 'Mr. David Nakato', '+256703456789', 'david.nakato@gmail.com', ARRAY['Esther Nakato'], 'Active')
ON CONFLICT (id) DO NOTHING;

-- 9. STUDENTS
INSERT INTO public.students (id, adm_no, fname, lname, dob, gender, student_type, class_id, guardian_id, guardian_name, guardian_phone, status) VALUES
(1, 'TAS-2026-001', 'Mary', 'Achola', '2014-03-12', 'Female', 'Day', 6, 1, 'Mr. George Achola', '+256701234567', 'Active'),
(2, 'TAS-2026-002', 'James', 'Okello', '2015-07-22', 'Male', 'Boarding', 4, 2, 'Mrs. Susan Okello', '+256702345678', 'Active'),
(3, 'TAS-2026-003', 'Esther', 'Nakato', '2012-11-05', 'Female', 'Day', 7, 3, 'Mr. David Nakato', '+256703456789', 'Active'),
(4, 'TAS-2026-004', 'David', 'Mukasa', '2012-04-14', 'Male', 'Day', 6, NULL, 'Mrs. Jane Mukasa', '+256706789012', 'Active'),
(5, 'TAS-2026-005', 'Grace', 'Atim', '2013-08-30', 'Female', 'Boarding', 5, NULL, 'Mr. Joseph Atim', '+256705678901', 'Active')
ON CONFLICT (id) DO NOTHING;

-- 10. FEE STRUCTURES
INSERT INTO public.fee_structures (id, category, student_type, amount, term, status) VALUES
(1, 'Tuition Fee', 'Day', 180000, 'Term 3', 'Active'),
(2, 'Development Fund', 'Day', 30000, 'Term 3', 'Active'),
(3, 'Medical Levy', 'Day', 15000, 'Term 3', 'Active'),
(4, 'Activity Fee', 'Day', 20000, 'Term 3', 'Active'),
(5, 'Tuition Fee', 'Boarding', 350000, 'Term 3', 'Active'),
(6, 'Boarding Fee', 'Boarding', 250000, 'Term 3', 'Active'),
(7, 'Meals & Catering', 'Boarding', 180000, 'Term 3', 'Active')
ON CONFLICT (id) DO NOTHING;

-- 11. PAYMENTS
INSERT INTO public.payments (id, receipt_no, student_id, student_name, class_id, amount, payment_method, payment_date, recorded_by, status, reference_no) VALUES
(1, 'RCP-2026-1047', 1, 'Mary Achola', 6, 450000, 'Cash', '2026-09-21', 'Mr. Okot David', 'Confirmed', NULL),
(2, 'RCP-2026-1046', 2, 'James Okello', 4, 380000, 'MTN Mobile Money', '2026-09-21', 'Mr. Okot David', 'Confirmed', 'MTN-00234567'),
(3, 'RCP-2026-1045', 3, 'Nakato Esther', 7, 450000, 'Airtel Money', '2026-09-20', 'Mr. Okot David', 'Confirmed', 'ATL-00123456')
ON CONFLICT (id) DO NOTHING;

-- 12. EXPENSES
INSERT INTO public.expenses (id, date, category, description, amount, requested_by, approved_by, status) VALUES
(1, '2026-09-20', 'Teaching Materials', 'Exercise books and pens - P.5 and P.6', 450000, 'Ms. Sarah Amoko', 'Mrs. Rose Nakato', 'Approved'),
(2, '2026-09-18', 'Utilities', 'Electricity bill - August 2026', 280000, 'Mr. Okot David', 'Mrs. Rose Nakato', 'Approved'),
(3, '2026-09-15', 'Maintenance', 'Repair of classroom roof - P.3 block', 850000, 'Mr. Charles Onen', 'Mrs. Rose Nakato', 'Approved')
ON CONFLICT (id) DO NOTHING;

-- 13. BOARDING DORMS
INSERT INTO public.boarding_dorms (id, name, gender, capacity, occupied) VALUES
(1, 'Dormitory Lion', 'Boys', 25, 22),
(2, 'Dormitory Elephant', 'Boys', 30, 26),
(3, 'Dormitory Giraffe', 'Girls', 25, 20),
(4, 'Dormitory Zebra', 'Girls', 20, 17)
ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, gender = EXCLUDED.gender, capacity = EXCLUDED.capacity, occupied = EXCLUDED.occupied;

-- 14. STAFF
INSERT INTO public.staff (id, name, role, department, phone, email, joined_date, status) VALUES
(1, 'Mrs. Rose Nakato', 'Headteacher', 'Administration', '+256701000001', 'r.nakato@tasaagaschool.org', '2018-01-15', 'Active'),
(2, 'Ms. Sarah Amoko', 'Teacher', 'Academics', '+256701000002', 's.amoko@tasaagaschool.org', '2020-02-01', 'Active'),
(3, 'Mr. James Opio', 'Teacher', 'Academics', '+256701000003', 'j.opio@tasaagaschool.org', '2019-08-15', 'Active'),
(4, 'Mr. David Okot', 'Finance Officer', 'Finance', '+256701000004', 'd.okot@tasaagaschool.org', '2021-03-01', 'Active')
ON CONFLICT (id) DO NOTHING;

-- 15. ANNOUNCEMENTS
INSERT INTO public.announcements (id, title, body, audience, published_by, date, status) VALUES
(1, 'End of Term Exams - Schedule', 'End of term examinations begin Monday 28th October 2026. All students must ensure outstanding fees are cleared before exam week.', 'All', 'Mrs. Rose Nakato', '2026-09-19', 'Published'),
(2, 'Parent-Teacher Meeting', 'A Parent-Teacher meeting is scheduled for Saturday 4th October 2026 at 10:00 AM in the school hall.', 'Parents', 'Mrs. Rose Nakato', '2026-09-15', 'Published')
ON CONFLICT (id) DO NOTHING;

-- 16. AUDIT LOGS
INSERT INTO public.audit_logs (id, timestamp, actor, action, entity, details, ip_address) VALUES
(1, NOW(), 'Mr. Okot David', 'PAYMENT_CONFIRM', 'Payment RCP-2026-1047', 'Confirmed UGX 450,000 for Achola Mary', '192.168.1.45'),
(2, NOW(), 'Mr. James Opio', 'RESULTS_SUBMIT', 'Results P.6A Term 3', 'Submitted assessment marks for 42 students', '192.168.1.32')
ON CONFLICT (id) DO NOTHING;

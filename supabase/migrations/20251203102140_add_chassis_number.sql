-- 1. Add chassis_number column
alter table public.cars add column chassis_number text;

-- 2. Add user_id column to link to authenticated users
alter table public.cars add column user_id uuid references auth.users(id) not null;

-- 3. Update created_at to ensure it has a default value (fixing the error)
alter table public.cars alter column created_at set default now();

-- 4. Update RLS policies for user-specific access

-- Drop old public-access policies
drop policy if exists "Enable read access for all users" on public.cars;
drop policy if exists "Enable insert for all users" on public.cars;

-- Create new policies based on user_id

-- Policy: Users can see all cars (still a public board)
create policy "Allow all users to view cars" on public.cars
for select using (true);

-- Policy: Authenticated users can insert cars for themselves
create policy "Allow authenticated users to insert their own cars" on public.cars
for insert to authenticated with check (auth.uid() = user_id);

-- Policy: Users can update their own cars
create policy "Allow users to update their own cars" on public.cars
for update to authenticated using (auth.uid() = user_id);

-- Policy: Users can delete their own cars
create policy "Allow users to delete their own cars" on public.cars
for delete to authenticated using (auth.uid() = user_id);

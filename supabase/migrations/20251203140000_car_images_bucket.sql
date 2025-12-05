-- Create car-images bucket
insert into storage.buckets (id, name, public)
values ('car-images', 'car-images', true)
on conflict (id) do nothing;

-- Policy: Public Read Access
create policy "Give public access to car images"
on storage.objects for select
using ( bucket_id = 'car-images' );

-- Policy: Authenticated Insert Access (User must own the file or just be authenticated)
-- Here we allow any authenticated user to upload car images
create policy "Allow authenticated uploads to car-images"
on storage.objects for insert
to authenticated
with check (
  bucket_id = 'car-images' AND
  auth.role() = 'authenticated'
);

-- Policy: Authenticated Update Access (User must own the file)
create policy "Allow users to update their own car images"
on storage.objects for update
to authenticated
using (
  bucket_id = 'car-images' AND
  auth.uid() = owner
);

-- Policy: Authenticated Delete Access
create policy "Allow users to delete their own car images"
on storage.objects for delete
to authenticated
using (
  bucket_id = 'car-images' AND
  auth.uid() = owner
);

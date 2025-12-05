-- Ensure the avatars bucket exists (in case it wasn't created correctly manually)
insert into storage.buckets (id, name, public)
values ('avatars', 'avatars', true)
on conflict (id) do nothing;

-- Policy: Public Read Access
create policy "Give public access to avatars"
on storage.objects for select
using ( bucket_id = 'avatars' );

-- Policy: Authenticated Insert Access (User must own the file)
create policy "Allow authenticated uploads"
on storage.objects for insert
to authenticated
with check (
  bucket_id = 'avatars' AND
  auth.uid() = owner
);

-- Policy: Authenticated Update Access
create policy "Allow authenticated updates"
on storage.objects for update
to authenticated
using (
  bucket_id = 'avatars' AND
  auth.uid() = owner
);

-- Policy: Authenticated Delete Access
create policy "Allow authenticated deletes"
on storage.objects for delete
to authenticated
using (
  bucket_id = 'avatars' AND
  auth.uid() = owner
);

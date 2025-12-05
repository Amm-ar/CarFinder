-- Force the avatars bucket to be public
update storage.buckets
set public = true
where id = 'avatars';

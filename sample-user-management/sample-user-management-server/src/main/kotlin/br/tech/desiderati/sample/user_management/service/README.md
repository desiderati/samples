# Email Uniqueness Validation Strategy

For now, the uniqueness validation of the email is handled by the **database**.

## Why was this approach adopted?

To allow experimentation and evaluation of the best strategy to use.

---

## Option A: Validate in the **service layer**

### ✅ Advantages:

- **Better user experience**: Allows for clearer and faster feedback, avoiding unnecessary trips to the database.
- **Business logic control**: All verification logic resides in one place (the service layer), making maintenance and
  testing easier.
- **Avoids unnecessary exceptions**: Prevents relying on database exceptions for predictable situations.

### ⚠️ Disadvantages:

- **Concurrency issues**: This approach is not race-condition safe. Two simultaneous processes may pass the check and
  attempt to insert the same email, causing a unique constraint violation in the database.
- **Performance**: Requires an additional query to the database just to check if the email already exists.

---

## Option B: Let the **database** enforce uniqueness, and handle it via a custom `ExceptionHandlingController`

### ✅ Advantages:

- **Guaranteed consistency**: The database ensures uniqueness, eliminating race condition issues.
- **Lower coupling**: No need for manual checks in the code — the responsibility is delegated to the database.
- **Performance**: Avoids extra queries to check email existence.

### ⚠️ Disadvantages:

- **Less friendly user experience**: The exception is thrown after the operation reaches the database, which may result
  in slower feedback.
- **More complex exception handling**: You must translate database exceptions into user-friendly messages.
- **Database dependency**: This approach assumes the constraint is correctly configured in the database, which can be a
  single point of failure if mismanaged.

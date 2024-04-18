from ..app import ma


class UserSchema(ma.Schema):
    class Meta:
        fields = ("id", "user_name")


user_schema = UserSchema()

from ..app import ma


class MessageSchema(ma.Schema):
    class Meta:
        fields = ("id", "sender_id", "recipient_id", "content", "added_date")


message_schema = MessageSchema()
messages_schema = MessageSchema(many=True)

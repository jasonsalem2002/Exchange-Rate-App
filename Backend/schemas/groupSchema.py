from marshmallow import Schema, fields, validate

class GroupSchema(Schema):
    id = fields.Integer(dump_only=True)
    name = fields.String(required=True, validate=validate.Length(max=100))

class GroupMessageSchema(Schema):
    id = fields.Integer(dump_only=True)
    group_id = fields.Integer(required=True)
    sender_id = fields.Integer(required=True)
    content = fields.String(required=True)
    added_date = fields.DateTime(dump_only=True)
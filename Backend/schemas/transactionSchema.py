from ..app import ma


class TransactionSchema(ma.Schema):
    class Meta:
        fields = (
            "id",
            "usd_amount",
            "lbp_amount",
            "usd_to_lbp",
            "added_date",
            "user_id",
        )


transaction_schema = TransactionSchema()
transactions_schema = TransactionSchema(many=True)

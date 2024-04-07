from ..app import ma

class OfferSchema(ma.Schema):
    class Meta:
        fields = ("id", "user_id", "amount_requested", "amount_to_trade", "usd_to_lbp", "added_date")

offer_schema = OfferSchema()
offers_schema = OfferSchema(many=True)
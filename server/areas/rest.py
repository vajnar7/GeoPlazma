from django.core.exceptions import ObjectDoesNotExist
from rest_framework import serializers
from rest_framework.permissions import IsAuthenticated, AllowAny
from rest_framework.response import Response
from rest_framework.views import APIView

from areas.models import Area, GeoPoint


class UpdateGeoPoint(APIView):
    permission_classes = (AllowAny,)

    def _get_area(self, name):
        try:
            return Area.objects.get(name=name)
        except ObjectDoesNotExist:
            return Area.objects.create(name=name)

    def _clear_all(self, area):
        qs = GeoPoint.objects.filter(area=area)
        for p in qs:
            p.delete()

    def get(self, request, format=None, area_name=None):
        area = self._get_area(area_name)
        # self._clear_all(area)
        # return Response({'cleared': True})
        res = {g.id: [g.lon, g.lat] for g in GeoPoint.objects.filter(area=area)}
        return Response(res)

    def post(self, request, format=None, area_name=None):
        lon = request.data.get("lon", 0)
        lat = request.data.get("lat", 0)
        area = self._get_area(area_name)
        print(lon)
        print(lat)
        print(area)

        GeoPoint.objects.create(area=area, lon=lon, lat=lat)
        return Response(dict(err_code="OK"))


class GeoPointSerializer(serializers.ModelSerializer):
    class Meta:
        model = GeoPoint
        fields = ('lon', 'lat')

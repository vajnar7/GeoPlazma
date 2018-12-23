from rest_framework.permissions import AllowAny
from rest_framework.response import Response
from rest_framework.views import APIView

from areas.models import Area, GeoPoint


class ObtainAreas(APIView):
    permission_classes = (AllowAny,)

    @staticmethod
    def get(request, f=None):
        res = []
        for a in Area.objects.all():
            res.append({a.name: {'lon': p.lon, 'lat': p.lat} for p in GeoPoint.objects.filter(area=a)})
        return Response(dict(areas=res))

    @staticmethod
    def put(request, f=None):
        res = []
        for a in Area.objects.all():
            res.append({a.name: {'lon': p.lon, 'lat': p.lat} for p in GeoPoint.objects.filter(area=a)})
        return Response(dict(areas=res))


class UpdateGeoPoint(APIView):
    permission_classes = (AllowAny,)

    @staticmethod
    def _get_area(name):
        try:
            return Area.objects.get(name=name)
        except:
            return Area.objects.create(name=name)

    @staticmethod
    def _clear_all(area):
        qs = GeoPoint.objects.filter(area=area)
        for p in qs:
            p.delete()

    def get(self, request, f=None, area_name=None):
        area = self._get_area(area_name)
        return Response({g.id: [g.lon, g.lat] for g in GeoPoint.objects.filter(area=area)})

    def post(self, request, f=None, area_name=None):
        lon = request.data.get("lon", 0)
        lat = request.data.get("lat", 0)
        area = self._get_area(area_name)
        print(lon)
        print(lat)
        print(area)

        GeoPoint.objects.create(area=area, lon=lon, lat=lat)
        return Response(dict(err_code="OK"))


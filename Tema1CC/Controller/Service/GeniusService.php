<?php

require_once "MetricsService.php";

class GeniusService
{
    private $keyPath = "C:\\Users\\adria\\XAMPP\\htdocs\\Tema1CC\\Controller\\Service\\Keys\\GeniusKey.txt";

    private function getKey()
    {
        $file = fopen($this->keyPath, 'r');
        $key = fgets($file);
        fclose($file);
        return $key;
    }

    function getArtistSongs($songId)
    {
        $metricsServ = new MetricsService();

        if ($songId == null) {
            $metricsServ->writeToLog("Genius", "GET song by random index", "500 INTERNAL SERVER ERROR", null, 0);
            return;
        }
        $artistId = 16775;

        $curl = curl_init();

        curl_setopt_array($curl, [
            CURLOPT_URL => "https://genius.p.rapidapi.com/artists/" . $artistId . "/songs",
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_FOLLOWLOCATION => true,
            CURLOPT_ENCODING => "",
            CURLOPT_MAXREDIRS => 10,
            CURLOPT_TIMEOUT => 30,
            CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
            CURLOPT_CUSTOMREQUEST => "GET",
            CURLOPT_HTTPHEADER => [
                "x-rapidapi-host: genius.p.rapidapi.com",
                "x-rapidapi-key: " . $this->getKey()
            ],
        ]);

        $start = microtime(true);
        $response = curl_exec($curl);
        $err = curl_error($curl);
        $timeElapsed = microtime(true) - $start;

        curl_close($curl);

        if ($err) {
            $metricsServ->writeToLog("Genius", "GET songs of artist with ID 16775", $err, null, $timeElapsed);
            return;
        } else {
            $fullTitle = json_decode($response)->response->songs[intval($songId)]->full_title;
            $metricsServ->writeToLog("Genius", "GET song with given ID by artist with ID 16675", "200OK", $fullTitle, $timeElapsed);
            return $fullTitle;
        }
    }
}

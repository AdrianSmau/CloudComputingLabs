<?php
require_once "Controller\Service\GeniusService.php";
require_once "Controller\Service\RandomOrgService.php";
require_once "Controller\Service\WebSearchService.php";
require_once "Controller\Service\MetricsService.php";
require_once "BaseController.php";

class MainController extends BaseController
{
    /**
     * "/getResults" Endpoint - runs a normal flow
     */
    public function getResultsAction()
    {
        $strErrorDesc = '';
        $requestMethod = $_SERVER["REQUEST_METHOD"];
        $results = null;

        if (strtoupper($requestMethod) == 'GET') {
            try {
                $MetricsServ = new MetricsService();
                //Uncomment when deploying
                $MetricsServ->clearLog();
                $MetricsServ->clearResultFile();

                $start = microtime(true);
                $RandomOrgServ = new RandomOrgService();
                $randomSongID = $RandomOrgServ->getRandomGeniusSongIndex();
                $GeniusServ = new GeniusService();
                $selectedSongTitle = $GeniusServ->getArtistSongs($randomSongID);
                $WebSearchServ = new WebSearchService();
                $results = json_encode($WebSearchServ->getWebSearches($selectedSongTitle));
                $timeElapsed = microtime(true) - $start;

                if ($results == null) {
                    $MetricsServ->writeToLog("getResults", "GET random number, generate random song by artist with id 16775 and fetch web searches", "500 INTERNAL SERVER ERROR", null, $timeElapsed);
                } else {
                    $MetricsServ->writeToLog("getResults", "GET random number, generate random song by artist with id 16775 and fetch web searches", "200OK", "Result file updated! Call /metrics for info!", $timeElapsed);
                }
            } catch (Error $e) {
                $strErrorDesc = $e->getMessage() . 'Something went wrong!';
                $strErrorHeader = 'HTTP/1.1 500 Internal Server Error';
            }
        } else {
            $strErrorDesc = 'Method not supported';
            $strErrorHeader = 'HTTP/1.1 422 Unprocessable Entity';
        }

        if (!$strErrorDesc) {
            $this->sendOutput(
                $results,
                array('Content-Type: application/json', 'HTTP/1.1 200 OK')
            );
        } else {
            $this->sendOutput(
                json_encode(array('error' => $strErrorDesc)),
                array('Content-Type: application/json', $strErrorHeader)
            );
        }
    }

    /**
     * "/metrics" Endpoint - displays log
     */

    public function metricsAction()
    {
        $strErrorDesc = '';
        $requestMethod = $_SERVER["REQUEST_METHOD"];
        $logs = null;

        if (strtoupper($requestMethod) == 'GET') {
            try {

                $MetricsServ = new MetricsService();

                $start = microtime(true);
                $logs = json_encode($MetricsServ->readLog());
                //$MetricsServ->readResultFile();
                $timeElapsed = microtime(true) - $start;

                if ($logs == null) {
                    $MetricsServ->writeToLog("metrics", "GET API metrics", "500 INTERNAL SERVER ERROR", null, $timeElapsed);
                } else {
                    $MetricsServ->writeToLog("metrics", "GET API metrics", "200OK", "Metrics displayed!", $timeElapsed);
                }
            } catch (Error $e) {
                $strErrorDesc = $e->getMessage() . 'Something went wrong!';
                $strErrorHeader = 'HTTP/1.1 500 Internal Server Error';
            }
        } else {
            $strErrorDesc = 'Method not supported';
            $strErrorHeader = 'HTTP/1.1 422 Unprocessable Entity';
        }

        if (!$strErrorDesc) {
            $this->sendOutput(
                $logs,
                array('Content-Type: application/json', 'HTTP/1.1 200 OK')
            );
        } else {
            $this->sendOutput(
                json_encode(array('error' => $strErrorDesc)),
                array('Content-Type: application/json', $strErrorHeader)
            );
        }
    }
    /**
     * "/parallelRun" Endpoint - runs 500 normal flows in batches of 50
     */
    public function parallelRunAction()
    {
        $strErrorDesc = '';
        $requestMethod = $_SERVER["REQUEST_METHOD"];
        $result = null;

        if (strtoupper($requestMethod) == 'GET') {
            try {
                $MetricsServ = new MetricsService();
                //Uncomment when deploying
                $MetricsServ->clearLog();
                $MetricsServ->clearResultFile();

                $start = microtime(true);
                $mh = curl_multi_init();

                $ch = curl_init();
                curl_setopt_array($ch, [
                    CURLOPT_URL => "http://localhost/Tema1CC/index.php/getResults",
                    CURLOPT_RETURNTRANSFER => true,
                    CURLOPT_FOLLOWLOCATION => true,
                    CURLOPT_ENCODING => "",
                    CURLOPT_MAXREDIRS => 10,
                    CURLOPT_TIMEOUT => 120,
                    CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
                    CURLOPT_CUSTOMREQUEST => "GET"
                ]);

                for ($i = 0; $i < 10; $i++) {
                    $curls = Array();
                    for ($j = 0; $j < 50; $j++) {
                        array_push($curls, clone $ch);
                        curl_multi_add_handle($mh, $curls[$j]);
                    }
                    do {
                        $status = curl_multi_exec($mh, $active);
                        if ($active) {
                            curl_multi_select($mh);
                        }
                    } while ($active && $status == CURLE_OK);
                    for ($j = 0; $j < 50; $j++) {
                        curl_multi_remove_handle($mh, $curls[$j]);
                    }
                }
                curl_multi_close($mh);
                $timeElapsed = microtime(true) - $start;
                $result = json_encode("Traffic captured! Check /metrics and result file!");

                if ($result == null) {
                    $MetricsServ->writeToLog("parallelRun", "GET 500 parralel runs", "500 INTERNAL SERVER ERROR", null, $timeElapsed);
                } else {
                    $MetricsServ->writeToLog("parallelRun", "GET 500 parralel runs", "200OK", "Result file updated! Call /metrics for info!", $timeElapsed);
                }
            } catch (Error $e) {
                $strErrorDesc = $e->getMessage() . 'Something went wrong!';
                $strErrorHeader = 'HTTP/1.1 500 Internal Server Error';
            }
        } else {
            $strErrorDesc = 'Method not supported';
            $strErrorHeader = 'HTTP/1.1 422 Unprocessable Entity';
        }

        if (!$strErrorDesc) {
            $this->sendOutput(
                $result,
                array('Content-Type: application/json', 'HTTP/1.1 200 OK')
            );
        } else {
            $this->sendOutput(
                json_encode(array('error' => $strErrorDesc)),
                array('Content-Type: application/json', $strErrorHeader)
            );
        }
    }
}

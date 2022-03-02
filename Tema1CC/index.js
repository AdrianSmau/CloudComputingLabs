const resultsContainer = document.querySelector('.resultsContainer');
const resultsButton = document.querySelector('.resultsButton')

const metricsContainer = document.querySelector('.metricsContainer');
const metricsButton = document.querySelector('.metricsButton')

const parallelRunContainer = document.querySelector('.parallelRunContainer');
const parallelRunButton = document.querySelector('.parallelRunButton')

function loadGetResults(){
    fetch('http://localhost/Tema1CC/index.php/getResults')
    .then(function(response){
        return response.json();
    })
    .then(function(myJson){
        myJson.forEach(function(json){
            const div = document.createElement('div');
            div.innerHTML = json;
            resultsContainer.appendChild(div);
        })
    })
}

function loadMetrics(){
    fetch('http://localhost/Tema1CC/index.php/metrics')
    .then(function(response){
        return response.json();
    })
    .then(function(myJson){
        myJson.forEach(function(json){
            const div = document.createElement('div');
            div.innerHTML = json;
            metricsContainer.appendChild(div);
        })
    })
}

function loadParallelRun(){
    fetch('http://localhost/Tema1CC/index.php/parallelRun')
    .then(function(response){
        return response.json();
    })
    .then(function(myJson){
        myJson.forEach(function(json){
            const div = document.createElement('div');
            div.innerHTML = json;
            parallelRunContainer.appendChild(div);
        })
    })
}

resultsButton.addEventListener('click', function(){
    if(resultsContainer.childElementCount > 1){
        while(resultsContainer.childElementCount > 1){
            resultsContainer.removeChild(resultsContainer.lastChild);
        }
    }
    loadGetResults();
})

metricsButton.addEventListener('click', function(){
    if(metricsContainer.childElementCount > 1){
        while(metricsContainer.childElementCount > 1){
            metricsContainer.removeChild(metricsContainer.lastChild);
        }
    }
    loadMetrics();
})

parallelRunButton.addEventListener('click', function(){
    if(parallelRunContainer.childElementCount > 1){
        while(parallelRunContainer.childElementCount > 1){
            parallelRunContainer.removeChild(parallelRunContainer.lastChild);
        }
    }
    loadParallelRun();
})
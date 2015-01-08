#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <semaphore.h>
#include <string.h>

#define TRUE 1
#define FALSE 0

/*Structure to hold thread Thread Arguements*/
struct THREAD_ARGS{
int Mvisitors;
int Ncars;
int Kpumps;
int Ttime;
int threadNum;
int done;// 1 true, 0 false
};

/*Structure for the cars*/
struct car{
	sem_t c;
	int rides;
	int time;
	int inService;// Boolean
	int cus;
	int inQueue;// Boolean
};

/*Structure for the pumps*/
struct pump{
	sem_t p;
	int inUse;// Boolean
	int carNum;
	int time;
	
};

/*Queue for the pump line*/
struct queue{
	sem_t q;
	int pQueue[200];
	int MAXsize;
	int front;
	int back;
	int size;
};

/*Functions to resemble a queue*/
int pop();
void push(int c);
int isEmpty();
struct queue pumpLine;

/*File to open*/
FILE *fp;


/*Global structures for the cars and pumps*/
struct car *cars;
struct pump *pumps;

/*Variable to let the other threads they are done*/
sem_t done;
int isDone;

/*Initialization functions*/
void initializeData(struct THREAD_ARGS *m);
void readInData(struct THREAD_ARGS *m);

/*Thread Functions*/
void *carThread(void *argv);
void *gasStationThread(void *argv);
void *visitorThread(void *argv);

int main()
{
	
	//struct THREAD_ARGS main;
	int i, M, N, K, T;
	
	struct THREAD_ARGS main;
	
	/*Create structures to hold the thread info*/
	struct THREAD_ARGS threadInfo[3];
	
	/*Create 3 threads to do work*/
	pthread_t thread[3];
	
	/*Semaphores to Initialize (not part of structures)*/
	sem_init(&(pumpLine.q), 0, 1);
	sem_init(&(done), 0, 1);
	
	/*Get data from file*/
	fp = fopen("data.txt", "r");
	
	if (fp == NULL) 
	{
  		fprintf(stderr, "Can't open input file in.list!\n");
  	exit(1);
	}
	
	readInData(&main);
	//initializeData(&main);
	
	
	while (main.done == FALSE)
	{
		printf("\n\nNEW Zoo Round\n\n");
		
		M = main.Mvisitors;
		N = main.Ncars;
		K = main.Kpumps;
		T = main.Ttime;
		
		printf("\nVisitors: %d, Cars: %d, Pumps: %d, Time: %d\n\n", M, N, K, T);
		
		
		/*Create the car structures*/
		cars = malloc(N * sizeof(struct car));
		for(i = 0; i < N; i++)
		{
			sem_init(&(cars[i].c), 0, 1);
			cars[i].rides = 0;
			cars[i].time = 0;
			cars[i].inService = FALSE;
			cars[i].cus = 0;
			cars[i].inQueue = FALSE;
		}
		
		/*Create the pump structures*/
		pumps = malloc(K * sizeof(struct pump));
		for(i = 0; i < K; i++)
		{
			sem_init(&(pumps[i].p), 0, 1);
			pumps[i].inUse = FALSE;
		}
		
		/*Initialize the Pump Queue*/
		pumpLine.front = 0;
		pumpLine.back = 0;
		pumpLine.size = 0;
		pumpLine.MAXsize = N;

		isDone = 0;
		
		/*Initialize the thread info*/
		for(i = 0; i < 4; i++)
		{
			threadInfo[i].Mvisitors = M;
			threadInfo[i].Ncars = N;
			threadInfo[i].Kpumps = K;
			threadInfo[i].Ttime = T;
			threadInfo[i].threadNum = i;
			threadInfo[i].done = FALSE;
			
		}
		
		/*Create the Visitor Thread*/
		pthread_create(&thread[0], NULL, visitorThread, (void *) &threadInfo[0]);
		
		/*Create the Car Thread*/
		pthread_create(&thread[1], NULL, carThread, (void *) &threadInfo[1]);
		
		/*Create the Gas Station Thread*/
		pthread_create(&thread[2], NULL, gasStationThread, (void *) &threadInfo[2]);
		
		
		/*Wait for the threads to finish*/
		for(i = 0; i < 3; i++)
			pthread_join(thread[i], NULL);
		
		/*Free dynamic memory*/	
		free(pumps);
		free(cars);
		
		readInData(&main);
		//initializeData(&main);
		
	}			
	return 0;

}

void initializeData(struct THREAD_ARGS *m)
{
	int M, N, K, T, i, j;
		M = m->Mvisitors;
		N = m->Ncars;
		K = m->Kpumps;
		T = m->Ttime;
		
		/*Create the car structures*/
		cars = malloc(N * sizeof(struct car));
		for(i = 0; i < N; i++)
		{
			sem_init(&(cars[i].c), 0, 1);
			cars[i].rides = 0;
			cars[i].time = 0;
			cars[i].inService = FALSE;
			cars[i].cus = 0;
		}
		
		/*Create the pump structures*/
		pumps = malloc(K * sizeof(struct pump));
		for(i = 0; i < K; i++)
		{
			sem_init(&(pumps[i].p), 0, 1);
			pumps[i].inUse = FALSE;
		}
		
		/*Initialize the Pump Queue*/
		pumpLine.front = 0;
		pumpLine.back = 0;
		pumpLine.MAXsize = N;

		isDone = 0;
}

void readInData(struct THREAD_ARGS *m)
{
	int v, n, k, t;	
	fscanf(fp, "%d, %d, %d, %d", &v, &n, &t, &k);
	m->Mvisitors = v;
	m->Ncars = n;
	m->Kpumps = k;
	m->Ttime = t;
	m->threadNum = 5;
	
	if (m->Mvisitors == 0)
	{
		m->done = TRUE;
		fclose(fp);
	}
	else
		m->done = FALSE;
	
}

/**************Functions for the QUEUE**************/
int pop()
{
	int f, fr;
	sem_wait(&(pumpLine.q));
	fr = pumpLine.front;
	f = pumpLine.pQueue[fr];
	printf("\n%d cars are waiting for pumps", pumpLine.size--);
	if( fr < (pumpLine.MAXsize - 1))
		pumpLine.front++;
	else 
		pumpLine.front = 0;
	
	sem_post(&(pumpLine.q));
	
	return f;
}

void push(int c)
{
	int b;
	sem_wait(&(pumpLine.q));
	b = pumpLine.back ;
	pumpLine.pQueue[b] = c;
	printf("\n%d cars are waiting for pumps", pumpLine.size++);
	if( b < (pumpLine.MAXsize - 1))
		pumpLine.back++;
	else 
		pumpLine.back = 0;
	sem_post(&(pumpLine.q));
}

int isEmpty()
{
	int boolean;
	sem_wait(&(pumpLine.q));
	if (pumpLine.front == pumpLine.back)
	{
		boolean = TRUE;
	}
	else	
		boolean = FALSE;
	sem_post(&(pumpLine.q));
	
	return boolean;
}


/*********************Thread Functions***********************/
void *visitorThread(void *argv)
{
	/*Initialize Data + Thread Arguements*/
	int v, c, p, t, id, i, j, s;
	struct THREAD_ARGS *visitor = (struct THREAD_ARGS *) argv; 
	v = visitor->Mvisitors;
	c = visitor->Ncars;
	p = visitor->Kpumps;
	t = visitor->Ttime;
	id = visitor->threadNum;
	
	/*As long as there is visitors, while will run*/
	i = 0;
	while(i < v)
	{
			for(j = 0; j < c; j++)
			{
				sem_wait(&(cars[j].c));
				if (cars[j].inService == FALSE && cars[j].rides < 5)
				{
					printf("\nVisior %d is on car %d", i+1, j+1);
					cars[j].inService = TRUE;
					i++;
					printf("\n%d customers waiting for a ride.", v - i);
					sem_post(&(cars[j].c));
					break;
				}
				sem_post(&(cars[j].c));
			}
		
			

	}
	
	/*Let the other threads know Visitor Thread is done*/
	sem_wait(&(done));
	isDone++;
	sem_post(&(done));
	

}

void *carThread(void *argv)
{
	/*Initialize Data + Thread Arguements*/
	int v, c, p, t, id, i, j, s, carsInServ, carsDone;
	struct THREAD_ARGS *carthread = (struct THREAD_ARGS *) argv; 
	v = carthread->Mvisitors;
	c = carthread->Ncars;
	p = carthread->Kpumps;
	t = carthread->Ttime;
	id = carthread->threadNum;
	
	carsDone = FALSE;
	
	while(carsDone == FALSE){
	carsInServ = 0;
	for(i = 0; i < c; i++)
	{
		//printf("Waiting for car %d", i); 
		sem_wait(&(cars[i].c));
		if (cars[i].inService == TRUE && cars[i].rides < 5)
		{
			//Car is in use, add more time
			cars[i].time++;
		}
		if (cars[i].time == t)
		{
			//Car reaches MAX time
			cars[i].inService = FALSE;
			cars[i].rides++;
			cars[i].time = 0;
			
		}
		if(cars[i].rides == 5 && cars[i].inQueue == FALSE)
		{
			//Car needs more gas, add to Queue
			push(i);
			cars[i].inQueue = TRUE;
		}
		if(cars[i].inService == FALSE)
			carsInServ++;
		sem_post(&(cars[i].c));
	}
	if (carsInServ == c && isDone >= 1)
		carsDone = TRUE;
	}

	sem_wait(&(done));
	isDone++;
	sem_post(&(done));
	
}

void *gasStationThread(void *argv)
{
	/*Initialize Data + Thread Arguements*/
	int v, c, p, t, id, i, j, s, truck = FALSE;
	int carsFilled = 0, pumpsInServ;
	struct THREAD_ARGS *gas = (struct THREAD_ARGS *) argv; 
	v = gas->Mvisitors;
	c = gas->Ncars;
	p = gas->Kpumps;
	t = gas->Ttime;
	id = gas->threadNum;
	int gasDone = FALSE;
	
	pumpsInServ = 0;
	while(gasDone == FALSE){
	
	for(i = 0; i < p; i++)
	{
		sem_wait(&(pumps[i].p));
		if (pumps[i].inUse == TRUE)
		{
			//Pump is being used, increment time
			pumps[i].time++;
		}
		if (pumps[i].time == 3)
		{
			//Pump has reached MAX time
			sem_wait(&(cars[i].c));
			cars[pumps[i].carNum].inService = FALSE;
			cars[pumps[i].carNum].rides = 0;
			cars[pumps[i].carNum].time = 0;
			cars[pumps[i].carNum].inQueue = FALSE;
			sem_post(&(cars[i].c));
			pumps[i].time = 0;
			pumps[i].inUse = FALSE;
			carsFilled++;
			pumpsInServ--;
			
		}
		if(truck == TRUE && pumps[i].inUse == FALSE)
		{
			//Truck fills up station first
			truck = FALSE;
			printf("\nFuel Truck is filling up station.");
		}
		else if(pumps[i].inUse == FALSE && pumpLine.size > 0)
		{
			//Add car to pump from queue
			pumpsInServ++;
			pumps[i].inUse = TRUE;
			pumps[i].carNum = pop();
			printf("\nCar %d is on pump %d", pumps[i].carNum + 1, i+1);
			pumps[i].time = 0;
		}
		sem_post(&(pumps[i].p));
	}
	
	if(carsFilled > 3)
	{
		printf("\nFuel truck has been called");
		truck = TRUE;
		carsFilled = 0;
	} 
	if (pumpsInServ == 0 && isDone == 2)
		gasDone = TRUE;
	}
	
	sem_wait(&(done));
	isDone++;
	sem_post(&(done));

}






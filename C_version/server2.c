/* A simple server in the internet domain using TCP
   The port number is passed as an argument */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h> 
#include <sys/socket.h>
#include <netinet/in.h>
#include <pthread.h>
#include <semaphore.h>
#include <curses.h>

struct textLine{
    char *lineOfText;
    int person; //0 = user. 1 = other person
    int lineNumber;
    struct textLine * next;
};
typedef struct textLine* chatLog;

struct connectionInfo{
    int * newsockfd;
    chatLog * visibleChat;
    chatLog * allChat;
    int * lineNumber;
};
typedef struct connectionInfo * info;

chatLog push(chatLog cl, char* text, int user, int lineNum);
chatLog pop(chatLog cl); //pop bottom of stack
void printChatLog(chatLog cl, int currentLine);
void printMessage(char *message, int time);

void error(const char *msg)
{
    perror(msg);
    exit(1);
}

void *listenForMessage(void *arg)
{
    int * newsockfd;
    newsockfd = ((info)arg) -> newsockfd;
    chatLog * visibleChat;
    visibleChat = ((info)arg) -> visibleChat;
    chatLog * allChat;
    allChat = ((info)arg) -> allChat;
    int * lineNumber;
    lineNumber = ((info)arg) -> lineNumber;
    int n;
    int cont = 1;
    while(cont)
    {
        move(LINES-1, 0);
        clrtoeol();
        printw("Message: ");
        refresh();
        char* buf = (char*) malloc(sizeof(char)*256);
        n = read(*newsockfd, buf, 255);
        if (n < 0) error("ERROR reading from socket");
        if(strcmp(buf, "exit()") == 0){
            cont = 0;
        }
        *allChat = push(*allChat, buf, 1, *lineNumber);
        *visibleChat = push(*visibleChat, buf, 1, *lineNumber);
        if((*visibleChat) -> lineNumber > LINES - 2){
            pop(*visibleChat);
            (*lineNumber)--;
        }
        printChatLog(*visibleChat, LINES - 3);
        (*lineNumber)++;
        refresh();
    }
    close(*newsockfd);
}

int main(int argc, char *argv[])
{
    initscr();
    char* message = "Welcome To InstaWorld!";
    printMessage(message, 1);
    chatLog visibleChat = NULL;
    chatLog allChat = NULL;
    int sockfd, newsockfd, portno;
    socklen_t clilen;
    struct sockaddr_in serv_addr, cli_addr;
    if (argc < 2) {
        fprintf(stderr,"ERROR, no port provided\n");
        exit(1);
    }
    sockfd = socket(AF_INET, SOCK_STREAM, 0);

    if (sockfd < 0) 
       error("ERROR opening socket");

    bzero((char *) &serv_addr, sizeof(serv_addr));
    portno = atoi(argv[1]);
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = INADDR_ANY;
    serv_addr.sin_port = htons(portno);

    if (bind(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0) 
        error("ERROR on binding");

    //ready to get information from client
    listen(sockfd,5);
    clilen = sizeof(cli_addr);
    newsockfd = accept(sockfd, (struct sockaddr *) &cli_addr, &clilen);
    if (newsockfd < 0){
        error("ERROR on accpt");
    }
    else{
        message = "Client Connection Successful";
        printMessage(message, 1);
    }
    pthread_t listener;
    int lineNumber = 1;
    info bufInfo;
    bufInfo -> newsockfd = &newsockfd;
    bufInfo -> visibleChat = &visibleChat;
    bufInfo -> allChat = &allChat;
    bufInfo -> lineNumber = &lineNumber;
    pthread_create(&listener, NULL, listenForMessage, bufInfo);
    int cont = 1;
    move(LINES-2,0);
    int x;
    int n;
    for(x = 0; x < COLS; x++){
        addch('_');
    }
    while(cont)
    {
        move(LINES-1, 0);
        clrtoeol();
        printw("Message: ");
        refresh();
        char* buf = (char*) malloc(sizeof(char)*256);
        getnstr(buf, 256);
        n = write(newsockfd, buf, strlen(buf));
        if (n < 0) error("ERROR writing to socket");
        if(strcmp(buf, "exit()") == 0){
            cont = 0;
        }
        allChat = push(allChat, buf, 0, lineNumber);
        visibleChat = push(visibleChat, buf, 0, lineNumber);
        if(visibleChat -> lineNumber > LINES - 2){
            pop(visibleChat);
            lineNumber--;
        }
        printChatLog(visibleChat, LINES - 3);
        lineNumber++;
        refresh();
    }
    message = "Goodbye";
    printMessage(message, 1);
    close(newsockfd);
    close(sockfd);
    endwin();
    return 0; 
}

void printMessage(char* message, int time){
    clear();
    move(LINES/3, COLS/3); // - (sizeof(message)/sizeof(char))*2);
    printw("%s", message);
    refresh();
    sleep(time);
    clear();
    refresh();
}

void printChatLog(chatLog cl, int currentLine){
    while(cl != NULL){
        if(currentLine > - 1){
            move(cl -> lineNumber - 1, 0);
            clrtoeol();
            char* buf = cl -> lineOfText;
            if(cl -> person){
                printw("Friend: %s", cl -> lineOfText);
            }
            else{
                printw("You: %s", cl -> lineOfText);
            }
        }
        cl = cl -> next;
        currentLine--;
    }
    return;
}

chatLog push(chatLog cl, char* text, int user, int lineNum)
{
    chatLog nl = (chatLog) malloc(sizeof(struct textLine));
    nl -> lineOfText = text;
    nl -> person = user;
    nl -> lineNumber = lineNum;
    nl -> next = cl;
    return nl;
}

chatLog pop(chatLog cl){ //never feed this a chatLog whose next is NULL
    cl -> lineNumber = cl -> lineNumber - 1;
    while(cl -> next -> next != NULL){
        pop(cl -> next);
        return;
    }
    free(cl -> next);
    cl -> next = NULL;
    return cl;
}
